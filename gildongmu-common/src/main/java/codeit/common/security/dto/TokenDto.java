package codeit.common.security.dto;

import lombok.Builder;


@Builder
public record TokenDto(
        String accessToken,
        String refreshToken
) {

}
