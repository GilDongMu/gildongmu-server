package codeit.api.participant.exception;

import codeit.api.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParticipantException extends RuntimeException {
    private final ErrorCode errorCode;
}