package codeit.api.auth.dto.response;

import codeit.common.security.dto.TokenDto;
import lombok.Builder;


@Builder
public record TokenResponse(
    String accessToken,
    String refreshToken
) {

    public static TokenResponse from(TokenDto dto) {
        return TokenResponse.builder()
            .accessToken(dto.accessToken())
            .refreshToken(dto.refreshToken())
            .build();
    }
}
