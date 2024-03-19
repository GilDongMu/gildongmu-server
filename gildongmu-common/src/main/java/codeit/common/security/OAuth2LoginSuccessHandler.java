package codeit.common.security;

import codeit.domain.user.constant.Role;
import codeit.domain.user.entity.User;
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
        User loginUser = oAuth2LoginUser.getUser();

        if (Role.ROLE_GUEST.equals(loginUser.getRole())) {
            response.sendRedirect("/oauth2/signup");
        } else if (Role.ROLE_USER.equals(loginUser.getRole())) {
            response.sendRedirect("/oauth2/token");
        }

    }
}
