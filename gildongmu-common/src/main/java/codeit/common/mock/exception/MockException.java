package codeit.common.mock.exception;

import codeit.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MockException extends RuntimeException {
    private final ErrorCode errorCode;
}