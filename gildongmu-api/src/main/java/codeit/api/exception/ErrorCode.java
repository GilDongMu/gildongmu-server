package codeit.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //global
    REQUEST_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "요청 파라미터를 확인해주세요."),
    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 유저가 없습니다."),
    ALREADY_REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    PASSWORD_UNMATCHED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_OAUTH2_USER(HttpStatus.NOT_FOUND, "oauth2 인증 유저가 아닙니다."),
    // room
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 소통 방이 없습니다."),
    UNAUTHORIZED_PARTICIPANTS(HttpStatus.BAD_REQUEST, "소통 방 권한이 없는 유저입니다."),
    //post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글이 없습니다."),
    // participant
    ALREADY_REGISTERED_PARTICIPANT(HttpStatus.BAD_REQUEST, "이미 참여 신청한 유저입니다."),
    PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 참여자가 없습니다."),
    NOT_LEADER_USER(HttpStatus.BAD_REQUEST, "해당 글 리더 유저가 아닙니다."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}

