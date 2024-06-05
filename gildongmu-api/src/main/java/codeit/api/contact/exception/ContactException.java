package codeit.api.contact.exception;

import codeit.api.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContactException extends RuntimeException {
    private final ErrorCode errorCode;
}