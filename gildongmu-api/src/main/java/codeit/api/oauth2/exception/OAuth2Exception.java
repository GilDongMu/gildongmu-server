package codeit.api.oauth2.exception;

import codeit.api.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuth2Exception extends RuntimeException {
    private final ErrorCode errorCode;
}