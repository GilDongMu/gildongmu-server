package codeit.api.participant.dto.response;

import codeit.domain.user.entity.User;
import lombok.Builder;

import java.util.Objects;


@Builder
public record ParticipantUserResponse(
        Long id,
        String nickname,
        String profilePath,
        boolean isMalicious,
        boolean isCurrentUser,
        boolean isDeleted
) {
    public static ParticipantUserResponse from(User user, Long currentUserId) {
        if (user.isDeleted()) {
            return ParticipantUserResponse.builder()
                    .nickname(null)
                    .id(user.getId())
                    .profilePath(null)
                    .isMalicious(user.isMalicious())
                    .isCurrentUser(Objects.equals(currentUserId, user.getId()))
                    .isDeleted(true)
                    .build();
        }
        return ParticipantUserResponse.builder()
                .nickname(user.getNickname())
                .id(user.getId())
                .profilePath(user.getProfilePath())
                .isMalicious(user.isMalicious())
                .isCurrentUser(Objects.equals(currentUserId, user.getId()))
                .build();
    }

    public static ParticipantUserResponse from(User user) {
        if (user.isDeleted()) {
            return ParticipantUserResponse.builder()
                    .nickname(null)
                    .id(user.getId())
                    .profilePath(null)
                    .build();
        }
        return ParticipantUserResponse.builder()
                .nickname(user.getNickname())
                .id(user.getId())
                .profilePath(user.getProfilePath())
                .build();
    }
}
