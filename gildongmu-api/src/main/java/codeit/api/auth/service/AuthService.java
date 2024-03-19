package codeit.api.auth.service;

import static codeit.api.exception.ErrorCode.ALREADY_REGISTERED_EMAIL;
import static codeit.api.exception.ErrorCode.PASSWORD_UNMATCHED;
import static codeit.api.exception.ErrorCode.USER_NOT_FOUND;

import codeit.api.auth.dto.request.LogInRequest;
import codeit.api.auth.dto.request.SignUpRequest;
import codeit.api.auth.dto.response.EmailCheckResponse;
import codeit.api.auth.dto.response.TokenResponse;
import codeit.api.auth.exception.AuthException;
import codeit.common.security.JwtTokenManager;
import codeit.domain.user.constant.Role;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;

    public void register(SignUpRequest request, MultipartFile profile) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException(ALREADY_REGISTERED_EMAIL);
        }

        userRepository.save(
            User.builder()
                .role(Role.ROLE_USER)
                .email(request.getEmail())
                .bio(request.getBio())
                .gender(request.getGender())
                .dateOfBirth(request.getDayOfBirth())
                .nickname(request.getNickname())
                .password(passwordEncoder.encode(request.getPassword()))
                .favoriteSpots(request.getFavoriteSpots())
                .build()
        );
    }

    public TokenResponse login(LogInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new AuthException(USER_NOT_FOUND));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException(PASSWORD_UNMATCHED);
        }

        return TokenResponse.from(jwtTokenManager.generate(user.getEmail()));
    }

    public EmailCheckResponse checkEmail(String email) {
        return EmailCheckResponse.of(!userRepository.existsByEmail(email));
    }
}
