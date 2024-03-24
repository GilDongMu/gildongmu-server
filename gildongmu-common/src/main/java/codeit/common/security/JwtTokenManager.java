package codeit.common.security;

import codeit.common.client.RedisClient;
import codeit.common.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Service
public class JwtTokenManager {
    private static SecretKey encryptKey;
    private static final long ACCESS_ALLOWANCE_TIME = 1000 * 60 * 60 * 24;
    private static final long REFRESH_ALLOWANCE_TIME = 1000 * 60 * 60 * 24 * 14;
    private final RedisClient redisClient;

    public JwtTokenManager(@Value("${jwt.secret}") String secret, RedisClient redisClient) {
        encryptKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.redisClient = redisClient;
    }

    public String generateAccessToken(String email) {
        return Jwts.builder()
                .subject(email)
                .expiration(new Date(System.currentTimeMillis() + ACCESS_ALLOWANCE_TIME))
                .signWith(encryptKey)
                .compact();
    }

    public String generateRefreshToken(String email) {
        String refreshToken = Jwts.builder()
                .expiration(new Date(System.currentTimeMillis() + REFRESH_ALLOWANCE_TIME))
                .signWith(encryptKey)
                .compact();
        redisClient.setValues(refreshToken, email, Duration.ofDays(14));
        return refreshToken;
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

    public String findEmailByRefreshToken(String refreshToken) {
        return redisClient.getValues(refreshToken)
                .orElseThrow(() -> new SecurityException(ErrorCode.AUTHENTICATION_FAILED.name()));
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
