package codeit.common.security;

import codeit.domain.user.constant.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2LoginUser oAuth2LoginUser = (OAuth2LoginUser) authentication.getPrincipal();

        if (oAuth2LoginUser.hasAuthority(Role.ROLE_GUEST)) {
            response.sendRedirect("/oauth2/signup");
        } else if (oAuth2LoginUser.hasAuthority(Role.ROLE_GUEST)) {
            response.sendRedirect("/oauth2/login");
        }

    }
}
