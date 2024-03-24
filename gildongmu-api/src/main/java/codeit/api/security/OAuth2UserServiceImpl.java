package codeit.api.security;

import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static codeit.domain.user.constant.Role.ROLE_GUEST;

@RequiredArgsConstructor
@Service
@Slf4j
public class OAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registration = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        String email = OAuth2Registration.valueOf(registration).getEmail(oAuth2User);
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder().email(email)
                        .role(ROLE_GUEST)
                        .build()));

        return new OAuth2LoginUser(oAuth2User, userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(), user);
    }
}
