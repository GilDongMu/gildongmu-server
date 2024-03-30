package codeit.api.oauth2.service;

import codeit.api.exception.ErrorCode;
import codeit.api.oauth2.dto.request.OAuth2SignUpRequest;
import codeit.api.oauth2.dto.response.TokenResponse;
import codeit.api.oauth2.exception.OAuth2Exception;
import codeit.api.security.OAuth2LoginUser;
import codeit.common.security.JwtTokenManager;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService {
    private final JwtTokenManager jwtTokenManager;
    private final UserRepository userRepository;
    @Transactional
    public TokenResponse register(OAuth2LoginUser oAuth2LoginUser, OAuth2SignUpRequest request, MultipartFile image) {
        User savedUser = userRepository.findById(oAuth2LoginUser.getUser().getId())
                .orElseThrow(() -> new OAuth2Exception(ErrorCode.USER_NOT_FOUND));

        savedUser.registerOAuth2User(request.getNickname(), request.getGender(),
                request.getDayOfBirth(), request.getBio(), request.getFavoriteSpots(), null);
        return issueToken(oAuth2LoginUser);
    }

    public TokenResponse issueToken(OAuth2LoginUser oAuth2LoginUser) {
        return Optional.ofNullable(oAuth2LoginUser)
                .map(oAuth2User -> TokenResponse.of(jwtTokenManager.generateAccessToken(oAuth2User.getUser().getEmail()),
                        jwtTokenManager.generateRefreshToken(oAuth2User.getUser().getEmail())))
                .orElseThrow(() -> new OAuth2Exception(ErrorCode.NOT_OAUTH2_USER));
    }
}
