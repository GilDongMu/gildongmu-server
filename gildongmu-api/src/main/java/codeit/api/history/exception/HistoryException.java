package codeit.api.history.exception;

import codeit.api.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HistoryException extends RuntimeException {
    private final ErrorCode errorCode;
}