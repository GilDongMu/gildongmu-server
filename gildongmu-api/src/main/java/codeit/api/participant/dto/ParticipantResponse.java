package codeit.api.participant.dto;

import codeit.domain.participant.entity.Participant;
import codeit.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;


@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ParticipantResponse(
        Long id,
        Boolean isLeader,
        ParticipantUserResponse user
) {
    public static ParticipantResponse from(Participant participant, Long currentUserId) {
        return ParticipantResponse.builder()
                .isLeader(participant.isLeader())
                .id(participant.getId())
                .user(ParticipantUserResponse.from(participant.getUser(), currentUserId))
                .build();
    }

    public static ParticipantResponse from(Participant participant) {
        return ParticipantResponse.builder()
                .id(participant.getId())
                .isLeader(participant.isLeader())
                .user(ParticipantUserResponse.from(participant.getUser()))
                .build();
    }

    public static ParticipantResponse of(Long participantId, User user) {
        return ParticipantResponse.builder()
                .id(participantId)
                .user(ParticipantUserResponse.from(user))
                .build();
    }
}
