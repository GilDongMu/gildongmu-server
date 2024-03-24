package codeit.api.security;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public enum OAuth2Registration {
    KAKAO {
        @Override
        public String getEmail(OAuth2User oAuth2User) {
            Map<String, String> map = oAuth2User.getAttribute("kakao_account");
            return map.get("email");
        }
    },
    GOOGLE {
        @Override
        public String getEmail(OAuth2User oAuth2User) {
            return oAuth2User.getAttribute("email");
        }
    };

    public abstract String getEmail(OAuth2User oAuth2User);
}
