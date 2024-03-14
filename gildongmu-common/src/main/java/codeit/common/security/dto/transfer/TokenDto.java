package codeit.common.security.dto.transfer;

import lombok.Builder;


@Builder
public record TokenDto(
        String accessToken,
        String refreshToken
) {

}
