package codeit.api.report.exception;

import codeit.api.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportException extends RuntimeException {
    private final ErrorCode errorCode;
}