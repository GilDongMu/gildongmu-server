package codeit.api.participant.controller;

import codeit.api.participant.ParticipantService;
import codeit.api.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}")
public class ParticipantController {
    private final ParticipantService participantService;

    @Operation(summary = "여행글 신청")
    @ApiResponse
    @PostMapping("/participants")
    private ResponseEntity<Void> applyForParticipant(
            @PathVariable Long postId, @AuthenticationPrincipal UserPrincipal principal) {

        participantService.applyForParticipant(postId, principal.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "여행글 신청 취소 및 참여 취소")
    @ApiResponse
    @DeleteMapping("/participants")
    private ResponseEntity<Void> exitParticipant(
            @PathVariable Long postId, @AuthenticationPrincipal UserPrincipal principal) {

        participantService.exitParticipant(postId, principal.getUser());
        return ResponseEntity.ok().build();
    }
}
