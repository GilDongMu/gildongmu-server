package codeit.api.config;

import codeit.common.security.UserPrincipal;
import codeit.domain.user.constant.Role;
import codeit.domain.user.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockCustomUser> {

    private static final UserPrincipal userPrincipal = new UserPrincipal(User.builder()
        .role(Role.ROLE_USER)
        .email("abcde@gmail.com")
        .build());

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userPrincipal, null, userPrincipal.getAuthorities());
        context.setAuthentication(authentication);
        return context;
    }
}
