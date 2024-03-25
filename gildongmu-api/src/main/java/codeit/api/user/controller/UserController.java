package codeit.api.user.controller;

import codeit.api.security.UserPrincipal;
import codeit.api.user.dto.request.PasswordCheckRequest;
import codeit.api.user.dto.request.UserProfileRequest;
import codeit.api.user.dto.response.PasswordCheckResponse;
import codeit.api.user.dto.response.UserProfileResponse;
import codeit.api.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/me")
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원 정보 조회")
    @ApiResponse
    @GetMapping
    private ResponseEntity<UserProfileResponse> retrieveMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.retrieveMyProfile(principal.getUser()));
    }

    @Operation(summary = "비밀번호 검증")
    @ApiResponse
    @PostMapping("/check-password")
    private ResponseEntity<PasswordCheckResponse> checkMyPassword(@Valid @RequestBody PasswordCheckRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.checkMyPassword(request, principal.getUser()));
    }

    @Operation(summary = "회원 정보 수정")
    @ApiResponse
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(encoding = @Encoding(name = "request", contentType = "application/json")))
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Void> modifyProfile(@Valid @RequestPart UserProfileRequest request,
                                               @RequestPart(required = false) MultipartFile image,
                                               @AuthenticationPrincipal UserPrincipal principal) {
        userService.modifyProfile(request, image, principal.getUser());
        return ResponseEntity.ok().build();
    }
}
