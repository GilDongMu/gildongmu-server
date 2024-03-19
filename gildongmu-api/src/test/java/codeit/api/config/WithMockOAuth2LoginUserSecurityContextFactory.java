package codeit.api.config;

import codeit.common.security.OAuth2LoginUser;
import codeit.domain.user.constant.Role;
import codeit.domain.user.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WithMockOAuth2LoginUserSecurityContextFactory implements
        WithSecurityContextFactory<WithMockOAuthLoginUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockOAuthLoginUser annotation) {
        OAuth2LoginUser oAuth2LoginUser = new OAuth2LoginUser(new DefaultOAuth2User(new ArrayList<>(), Map.of("any", "any"), "any"), "any", User.builder()
                .role(Role.ROLE_USER)
                .email("abcde@gmail.com")
                .build());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                oAuth2LoginUser, null, oAuth2LoginUser.getAuthorities());
        context.setAuthentication(authentication);
        return context;
    }
}
