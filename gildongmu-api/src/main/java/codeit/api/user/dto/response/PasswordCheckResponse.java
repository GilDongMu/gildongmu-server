package codeit.api.user.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record PasswordCheckResponse(
        boolean isCorrect,
        LocalDateTime timestamp
) {
    public static PasswordCheckResponse of(boolean isCorrect) {
        return PasswordCheckResponse.builder()
                .isCorrect(isCorrect)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
