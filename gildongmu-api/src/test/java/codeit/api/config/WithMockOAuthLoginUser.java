package codeit.api.config;

import codeit.domain.user.constant.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockOAuth2LoginUserSecurityContextFactory.class)
public @interface WithMockOAuthLoginUser {
    Role role() default Role.ROLE_USER;
}
