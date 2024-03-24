package codeit.api.room.exception;

import codeit.api.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomException extends RuntimeException {
    private final ErrorCode errorCode;
}