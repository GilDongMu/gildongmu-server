package codeit.api.post.exception;

import codeit.api.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostException extends RuntimeException {
    private final ErrorCode errorCode;
}
