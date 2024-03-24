package codeit.api.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.http.ResponseCookie;

import java.time.Duration;


@Builder
public record TokenResponse(
        String accessToken,
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String refreshToken
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
