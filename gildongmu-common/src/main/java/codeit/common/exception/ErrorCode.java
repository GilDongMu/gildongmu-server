package codeit.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // user
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 유저가 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}

