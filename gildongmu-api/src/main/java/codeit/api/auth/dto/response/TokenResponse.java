package codeit.api.auth.dto.response;

import codeit.common.security.CookieUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


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
        return CookieUtil.generateCookie("refreshToken", refreshToken);
    }
}
