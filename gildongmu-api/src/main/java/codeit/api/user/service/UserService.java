package codeit.api.user.service;

import codeit.api.exception.ErrorCode;
import codeit.api.user.dto.request.PasswordCheckRequest;
import codeit.api.user.dto.request.UserProfileRequest;
import codeit.api.user.dto.response.PasswordCheckResponse;
import codeit.api.user.dto.response.UserProfileResponse;
import codeit.api.user.exception.UserException;
import codeit.common.client.S3Client;
import codeit.domain.chat.repository.ChatMongoRepository;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Client s3Client;
    private final ChatMongoRepository chatMongoRepository;

    public UserProfileResponse retrieveMyProfile(User user) {
        return UserProfileResponse.from(user);
    }

    @Transactional
    public void modifyProfile(UserProfileRequest request, MultipartFile image, User user) {
        User dbUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        dbUser.update(request.getNickname(), request.getBio(), request.getFavoriteSpots());
        updatePasswordIfNeeded(request.getIsPasswordChanged(), request.getPassword(), dbUser);
        updateProfileImageIfNeeded(Optional.ofNullable(image), dbUser);

        chatMongoRepository.saveAll(chatMongoRepository.findBySenderUserId(user.getId())
                .stream().map(chat -> chat.updateChatUserProfile(user)).collect(Collectors.toList()));
    }

    public void updatePasswordIfNeeded(Boolean isPasswordChanged, Optional<String> maybePassword, User dbUser) {
        if (isPasswordChanged)
            dbUser.updatePassword(passwordEncoder.encode(
                    maybePassword.orElseThrow(() -> new UserException(ErrorCode.PASSWORD_NOT_VALID))));
    }

    public void updateProfileImageIfNeeded(Optional<MultipartFile> maybeImage, User dbUser) {
        if (maybeImage.isEmpty()) return;
        s3Client.delete(dbUser.getProfilePath());
        dbUser.updateProfilePath(s3Client.upload(maybeImage.get()));
    }

    public PasswordCheckResponse checkMyPassword(PasswordCheckRequest request, User user) {
        User dbUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        return PasswordCheckResponse.of(passwordEncoder.matches(request.getPassword(), dbUser.getPassword()));
    }
}
