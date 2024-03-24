package codeit.api.oauth2.dto.response;

import codeit.common.security.CookieUtil;
import lombok.Builder;
import net.minidev.json.annotate.JsonIgnore;


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
        return CookieUtil.generateCookie("refreshToken", refreshToken);
    }
}
