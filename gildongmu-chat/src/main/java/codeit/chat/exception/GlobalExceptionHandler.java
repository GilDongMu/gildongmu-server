package codeit.chat.exception;

import codeit.common.security.dto.response.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String LOG_FORMAT = " Code : {}, Message : {}";

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public ExceptionResponse<String> handleException(Exception e) {
        log.error(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage());
        return new ExceptionResponse<>(e.getClass().getSimpleName(), e.getMessage());
    }
}
