package codeit.chat.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatException extends RuntimeException {
    private final ErrorCode errorCode;
}