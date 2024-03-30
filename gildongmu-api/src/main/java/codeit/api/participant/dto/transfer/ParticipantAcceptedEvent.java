package codeit.api.participant.dto.transfer;

import lombok.Builder;

@Builder
public record ParticipantAcceptedEvent(
        Long roomId,
        Long userId
) {
}
