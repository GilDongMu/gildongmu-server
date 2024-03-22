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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
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
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "OAuth2 로그인 유저 토큰 발급")
    @ApiResponse
    @GetMapping("/login")
    private ResponseEntity<TokenResponse> issueToken(@AuthenticationPrincipal OAuth2LoginUser oAuth2LoginUser) {
        return ResponseEntity.ok(oAuth2LoginService.issueToken(oAuth2LoginUser));
    }
}
