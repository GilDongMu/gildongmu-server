package codeit.api.participant.controller;

import codeit.api.participant.dto.ParticipantResponse;
import codeit.api.participant.service.ParticipantService;
import codeit.api.security.UserPrincipal;
import codeit.common.validator.EnumValue;
import codeit.domain.participant.constant.ParticipantStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}")
public class ParticipantController {
    private final ParticipantService participantService;

    @Operation(summary = "여행글 신청")
    @ApiResponse
    @PostMapping("/participants")
    public ResponseEntity<Void> applyForParticipant(
            @PathVariable Long postId, @AuthenticationPrincipal UserPrincipal principal) {

        participantService.applyForParticipant(postId, principal.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "여행글 신청 취소 및 참여 취소")
    @ApiResponse
    @DeleteMapping("/participants")
    public ResponseEntity<Void> exitParticipant(
            @PathVariable Long postId, @AuthenticationPrincipal UserPrincipal principal) {

        participantService.exitParticipant(postId, principal.getUser());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "신청자 거절 및 추방")
    @ApiResponse
    @DeleteMapping("/participants/{participantId}")
    public ResponseEntity<Void> denyParticipant(
            @PathVariable Long postId,
            @PathVariable Long participantId,
            @AuthenticationPrincipal UserPrincipal principal) {

        participantService.denyParticipant(postId, participantId, principal.getUser());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "신청자 수락")
    @ApiResponse
    @PutMapping("/participants/{participantId}")
    public ResponseEntity<Void> acceptParticipant(
            @PathVariable Long postId,
            @PathVariable Long participantId,
            @AuthenticationPrincipal UserPrincipal principal) {

        participantService.acceptParticipant(postId, participantId, principal.getUser());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "참여자 목록 조회")
    @ApiResponse
    @GetMapping("/participants")
    public ResponseEntity<List<ParticipantResponse>> retrieveParticipants(
            @PathVariable Long postId, @RequestParam @EnumValue(enumClass = ParticipantStatus.class) String status,
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(participantService.retrieveParticipants(postId, principal.getUser(), status));
    }
}
