package codeit.common.security;

import codeit.domain.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;

@Getter
public class OAuth2LoginUser extends DefaultOAuth2User {
    private final User user;

    public OAuth2LoginUser(OAuth2User oAuth2User, String nameAttributeKey, User user) {
        super(Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                oAuth2User.getAttributes(), nameAttributeKey);
        this.user = user;
    }
}
