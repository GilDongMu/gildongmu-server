package codeit.api.comment.exception;

import codeit.api.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentException extends RuntimeException{
    private final ErrorCode errorCode;
}
