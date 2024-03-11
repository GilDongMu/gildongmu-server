package codeit.api.mock.exception;

import codeit.api.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MockException extends RuntimeException {
    private final ErrorCode errorCode;
}