package codeit.api.auth.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;


@Builder
public record EmailCheckResponse(
    boolean isUsable,
    LocalDateTime timestamp
) {
    public static EmailCheckResponse of(boolean isUsable) {
        return EmailCheckResponse.builder()
            .isUsable(isUsable)
            .timestamp(LocalDateTime.now())
            .build();
    }
}
