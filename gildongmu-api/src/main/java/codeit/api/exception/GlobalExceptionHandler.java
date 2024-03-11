package codeit.api.exception;

import codeit.api.mock.exception.MockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String LOG_FORMAT = "Class : {}, Code : {}, Message : {}";

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
