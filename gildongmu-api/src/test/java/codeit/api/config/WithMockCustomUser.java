package codeit.api.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import codeit.domain.user.constant.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    Role role() default Role.ROLE_USER;
}
