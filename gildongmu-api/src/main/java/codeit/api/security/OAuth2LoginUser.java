package codeit.api.security;

import codeit.domain.user.constant.Role;
import codeit.domain.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class OAuth2LoginUser implements OAuth2User {
    @Getter
    private final User user;
    private final List<GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;

    public OAuth2LoginUser(OAuth2User oAuth2User, String nameAttributeKey, User user) {
        authorities = new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())));
        attributes = oAuth2User.getAttributes();
        this.nameAttributeKey = nameAttributeKey;
        this.user = user;
    }

    public boolean hasAuthority(Role role) {
        return authorities.stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role.name()));
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return nameAttributeKey;
    }

    public void authorizeUser() {
        this.authorities.remove(authorities.size() - 1);
        this.authorities.add(new SimpleGrantedAuthority(Role.ROLE_USER.name()));
    }
}
