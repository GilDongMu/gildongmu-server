package codeit.api.oauth2.dto.response;

import lombok.Builder;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.http.ResponseCookie;

import java.time.Duration;


@Builder
public record TokenResponse(
        String accessToken,
        @JsonIgnore String refreshToken
) {

    public static TokenResponse of(String accessToken, String refreshToken) {
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String generateCookie() {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .maxAge(Duration.ofDays(30))
                .path("/")
                .build().toString();
    }
}
