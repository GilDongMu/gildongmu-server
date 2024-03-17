package codeit.api.oauth2.dto.response;

import codeit.common.security.dto.transfer.TokenDto;
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
