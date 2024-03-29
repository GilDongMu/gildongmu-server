package codeit.api.security;

import codeit.api.oauth2.dto.response.OAuth2UserInfoResponse;
import codeit.api.oauth2.dto.response.TokenResponse;
import codeit.api.oauth2.service.OAuth2LoginService;
import codeit.domain.user.constant.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2LoginService oAuth2LoginService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2LoginUser oAuth2LoginUser = (OAuth2LoginUser) authentication.getPrincipal();
        if (oAuth2LoginUser.hasAuthority(Role.ROLE_GUEST)) {
            response.setStatus(HttpStatus.CREATED.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(OAuth2UserInfoResponse.from(oAuth2LoginUser.getUser())));
            getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/oauth2/signup");
        } else if (oAuth2LoginUser.hasAuthority(Role.ROLE_USER)) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            TokenResponse tokenResponse = oAuth2LoginService.issueToken(oAuth2LoginUser);
            response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));
            response.setHeader(HttpHeaders.SET_COOKIE, tokenResponse.generateCookie());
            getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/oauth2/login");
        }
    }


}
