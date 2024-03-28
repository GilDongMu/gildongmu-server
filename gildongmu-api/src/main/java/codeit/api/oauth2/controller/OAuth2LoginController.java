package codeit.api.oauth2.controller;

import codeit.api.oauth2.dto.request.OAuth2SignUpRequest;
import codeit.api.oauth2.dto.response.OAuth2UserInfoResponse;
import codeit.api.oauth2.dto.response.TokenResponse;
import codeit.api.oauth2.service.OAuth2LoginService;
import codeit.api.security.OAuth2LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
public class OAuth2LoginController {

    private final OAuth2LoginService oAuth2LoginService;

    @GetMapping("/signup")
    private ResponseEntity<OAuth2UserInfoResponse> getOAuth2UserInfo(@AuthenticationPrincipal OAuth2LoginUser oAuth2LoginUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OAuth2UserInfoResponse.from(oAuth2LoginUser.getUser()));
    }

    @Operation(summary = "OAuth2 로그인 유저의 회원가입 폼 작성")
    @ApiResponse
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(encoding = @Encoding(name = "request", contentType = "application/json")))
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Void> register(@AuthenticationPrincipal OAuth2LoginUser oAuth2LoginUser,
                                          @Valid @RequestPart OAuth2SignUpRequest request,
                                          @Valid @RequestPart(required = false) MultipartFile image) {
        oAuth2LoginService.register(oAuth2LoginUser.getUser(), request, image);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header(HttpHeaders.LOCATION, "/oauth2/login")
                .build();
    }


    @Operation(summary = "OAuth2 로그인 유저 토큰 발급")
    @ApiResponse
    @GetMapping("/login")
    private ResponseEntity<TokenResponse> issueToken(@AuthenticationPrincipal OAuth2LoginUser oAuth2LoginUser) {
        TokenResponse response = oAuth2LoginService.issueToken(oAuth2LoginUser);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, response.generateCookie())
                .body(response);
    }
}
