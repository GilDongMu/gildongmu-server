package codeit.api.security;

import codeit.common.security.CookieUtil;
import codeit.common.security.JwtTokenManager;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static codeit.common.exception.ErrorCode.AUTHENTICATION_FAILED;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX = "Bearer ";
    private final UserDetailsService userDetailsService;
    private final JwtTokenManager jwtTokenManager;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        getToken(request).ifPresent(
                accessToken -> {
                    try {
                        if (StringUtils.hasText(accessToken) && JwtTokenManager.validate(accessToken))
                            authorize(accessToken);
                    } catch (ExpiredJwtException e) {
                        authorizeIfValidRefreshToken(request, response);
                    }
                }
        );
        filterChain.doFilter(request, response);
    }

    private void authorizeIfValidRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String email = CookieUtil.getCookieValue(request.getCookies(), "refreshToken")
                .map(jwtTokenManager::findEmailByRefreshToken)
                .orElseThrow(() -> new SecurityException(AUTHENTICATION_FAILED.getMessage()));
        String newAccessToken = jwtTokenManager.generateAccessToken(email);
        String newRefreshToken = jwtTokenManager.generateRefreshToken(email);
        response.setHeader(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + newAccessToken);
        response.setHeader(HttpHeaders.SET_COOKIE, CookieUtil.generateCookie("refreshToken", newRefreshToken));
        authorize(newAccessToken);
    }

    private Optional<String> getToken(HttpServletRequest request) {
        String rawToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!ObjectUtils.isEmpty(rawToken) && rawToken.startsWith(TOKEN_PREFIX))
            return Optional.of(rawToken.substring(TOKEN_PREFIX.length()));
        return Optional.empty();
    }

    public void authorize(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(JwtTokenManager.parseEmail(token));
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
    }
}
