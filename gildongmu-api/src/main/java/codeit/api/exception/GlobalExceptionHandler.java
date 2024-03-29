package codeit.api.exception;

import codeit.api.auth.exception.AuthException;
import codeit.api.bookmark.exception.BookmarkException;
import codeit.api.mock.exception.MockException;
import codeit.api.oauth2.exception.OAuth2Exception;
import codeit.api.participant.exception.ParticipantException;
import codeit.api.post.exception.PostException;
import codeit.api.room.exception.RoomException;
import codeit.api.user.exception.UserException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static codeit.api.exception.ErrorCode.REQUEST_ARGUMENT_NOT_VALID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse<List<String>>> handleUserException(ConstraintViolationException e) {
        List<String> messages = e.getConstraintViolations()
                .stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        log.info(LOG_FORMAT, e.getClass().getSimpleName(), REQUEST_ARGUMENT_NOT_VALID, REQUEST_ARGUMENT_NOT_VALID.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse<>(REQUEST_ARGUMENT_NOT_VALID.name(), messages));
    }

    @ExceptionHandler(ParticipantException.class)
    public ResponseEntity<ExceptionResponse<String>> handleParticipantException(ParticipantException e) {
        log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getErrorCode(), e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new ExceptionResponse<>(e.getErrorCode().name(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ExceptionResponse<String>> handleUserException(UserException e) {
        log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getErrorCode(), e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new ExceptionResponse<>(e.getErrorCode().name(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(RoomException.class)
    public ResponseEntity<ExceptionResponse<String>> handleRoomException(RoomException e) {
        log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getErrorCode(), e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new ExceptionResponse<>(e.getErrorCode().name(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(OAuth2Exception.class)
    public ResponseEntity<ExceptionResponse<String>> handleOAuth2Exception(OAuth2Exception e) {
        log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getErrorCode(), e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new ExceptionResponse<>(e.getErrorCode().name(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ExceptionResponse<String>> handleAuthException(AuthException e) {
        log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getErrorCode(), e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new ExceptionResponse<>(e.getErrorCode().name(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<ExceptionResponse<String>> handlePostException(PostException e) {
        log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getErrorCode(), e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(new ExceptionResponse<>(e.getErrorCode().name(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(BookmarkException.class)
    public ResponseEntity<ExceptionResponse<String>> handleBookmarkException(BookmarkException e) {
        log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getErrorCode(), e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
            .body(new ExceptionResponse<>(e.getErrorCode().name(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse<List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> messages = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        log.info(LOG_FORMAT, e.getClass().getSimpleName(), REQUEST_ARGUMENT_NOT_VALID, REQUEST_ARGUMENT_NOT_VALID.getMessage());
        return ResponseEntity.status(e.getStatusCode())
                .body(new ExceptionResponse<>(REQUEST_ARGUMENT_NOT_VALID.name(), messages));
    }

    @ExceptionHandler(MockException.class)
    public ResponseEntity<ExceptionResponse<String>> handleMockException(MockException e) {
        log.info(LOG_FORMAT, e.getClass().getSimpleName(), e.getErrorCode(), e.getErrorCode().getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(new ExceptionResponse<>(e.getErrorCode().name(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse<String>> handleException(Exception e) {
        log.error(LOG_FORMAT, e.getClass().getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage()));
    }
}
