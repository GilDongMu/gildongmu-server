package codeit.api.bookmark.exception;

import codeit.api.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookmarkException extends RuntimeException {
    private final ErrorCode errorCode;
}