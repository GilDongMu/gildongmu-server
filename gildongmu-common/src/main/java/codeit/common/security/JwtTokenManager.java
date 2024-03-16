package codeit.common.security;

import codeit.common.security.dto.transfer.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtTokenManager {
    private static SecretKey encryptKey;
    private static final long ACCESS_ALLOWANCE_TIME = 1000 * 60 * 60 * 24;
    private static final long REFRESH_ALLOWANCE_TIME = 1000 * 60 * 60 * 24 * 14;

    public JwtTokenManager(@Value("${jwt.secret}") String secret) {
        encryptKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public TokenDto generate(String email) {
        long now = System.currentTimeMillis();
        String accessToken = Jwts.builder()
                .subject(email)
                .expiration(new Date(now + ACCESS_ALLOWANCE_TIME))
                .signWith(encryptKey)
                .compact();

        String refreshToken = Jwts.builder()
                .expiration(new Date(now + REFRESH_ALLOWANCE_TIME))
                .signWith(encryptKey)
                .compact();

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static boolean validate(String token) {
        try {
            return getClaims(token).getExpiration().after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    public static String parseEmail(String token) {
        return getClaims(token).getSubject();
    }

    private static Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(encryptKey)
                .build()
                .parseSignedClaims(token).getPayload();
    }
}
