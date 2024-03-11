package codeit.api.mock.controller;

import codeit.api.exception.ErrorCode;
import codeit.api.mock.exception.MockException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MockController {
    @GetMapping("/mock")
    public ResponseEntity<Void> test() {
        throw new MockException(ErrorCode.DOMAIN_NOT_FOUND);
    }
}
