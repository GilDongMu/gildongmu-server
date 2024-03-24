package codeit.api.user.service;

import codeit.api.exception.ErrorCode;
import codeit.api.user.dto.request.PasswordCheckRequest;
import codeit.api.user.dto.request.UserProfileRequest;
import codeit.api.user.dto.response.PasswordCheckResponse;
import codeit.api.user.dto.response.UserProfileResponse;
import codeit.api.user.exception.UserException;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfileResponse retrieveMyProfile(User user) {
        return UserProfileResponse.from(user);
    }

    @Transactional
    public void modifyProfile(UserProfileRequest request, MultipartFile image, User user) {
        User dbUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        dbUser.update(request.getNickname(), request.getBio(), request.getFavoriteSpots(), null);
        if (request.isPasswordChanged())
            dbUser.updatePassword(passwordEncoder.encode(request.getPassword()));
    }

    public PasswordCheckResponse checkMyPassword(PasswordCheckRequest request, User user) {
        return PasswordCheckResponse.of(passwordEncoder.matches(request.getPassword(), user.getPassword()));
    }
}
