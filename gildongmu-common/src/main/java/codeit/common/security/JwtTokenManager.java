package codeit.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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

    public String generateAccessToken(String email) {
        return Jwts.builder()
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + ACCESS_ALLOWANCE_TIME))
                .signWith(encryptKey)
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .expiration(new Date(System.currentTimeMillis() + REFRESH_ALLOWANCE_TIME))
                .signWith(encryptKey)
                .compact();
    }


    public static boolean validate(String token) {
        try {
            return getClaims(token).getExpiration().after(new Date());
        } catch (ExpiredJwtException e) {
            throw e;
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
