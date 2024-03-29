package codeit.api.auth.controller;

import codeit.api.auth.dto.request.EmailCheckRequest;
import codeit.api.auth.dto.request.LogInRequest;
import codeit.api.auth.dto.request.SignUpRequest;
import codeit.api.auth.dto.response.EmailCheckResponse;
import codeit.api.auth.dto.response.TokenResponse;
import codeit.api.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입")
    @ApiResponse
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(encoding = @Encoding(name = "request", contentType = "application/json")))
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Void> register(@Valid @RequestPart SignUpRequest request,
                                          @Valid @RequestPart(required = false) MultipartFile profile) {
        authService.register(request, profile);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "로그인")
    @ApiResponse
    @PostMapping("/login")
    private ResponseEntity<TokenResponse> login(@Valid @RequestBody LogInRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, response.generateCookie())
                .body(response);
    }

    @Operation(summary = "이메일 중복 검사")
    @ApiResponse
    @PostMapping("/check-email")
    private ResponseEntity<EmailCheckResponse> checkEmail(
            @Valid @RequestBody EmailCheckRequest request) {
        return ResponseEntity.ok(authService.checkEmail(request.getEmail()));
    }

}
