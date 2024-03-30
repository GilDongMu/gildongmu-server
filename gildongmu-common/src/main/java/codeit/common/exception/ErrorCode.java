package codeit.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // global
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다."),
    // user
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당하는 유저가 없습니다."),
    // s3
    FILE_CONVERT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 변환에 실패하였습니다."),
    WRONG_FILE_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 형식의 확장자입니다."),
    UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "업로드에 실패하였습니다."),
    DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "삭제에 실패하였습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}

