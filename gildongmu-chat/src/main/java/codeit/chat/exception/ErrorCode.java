package codeit.chat.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 유저가 없습니다."),
    NOT_AUTHENTICATED_USER(HttpStatus.UNAUTHORIZED, "검증되지 않은 유저입니다."),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 소통 방이 없습니다."),
    UNAUTHORIZED_PARTICIPANTS(HttpStatus.BAD_REQUEST, "소통 방 권한이 없는 유저입니다."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
