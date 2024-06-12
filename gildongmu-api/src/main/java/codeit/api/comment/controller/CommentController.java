package codeit.api.comment.controller;

import codeit.api.alert.service.FirebaseNotificationService;
import codeit.api.comment.dto.request.CommentCreateRequest;
import codeit.api.comment.dto.request.CommentUpdateRequest;
import codeit.api.comment.dto.response.CommentListResponse;
import codeit.api.comment.dto.response.CommentUpdateResponse;
import codeit.api.comment.service.CommentService;
import codeit.api.security.UserPrincipal;
import codeit.domain.post.repository.PostRepository;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentController {

    private final CommentService commentService;
    private final PostRepository postRepository;
    private final FirebaseNotificationService firebaseNotificationService;

    @Operation(summary = "댓글 생성")
    @ApiResponse
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Void> createComment(
            @AuthenticationPrincipal UserPrincipal auth,
            @PathVariable("postId") Long postId,
            @RequestBody @Valid CommentCreateRequest commentCreateRequest)
        throws FirebaseMessagingException {

        commentService.createComment(commentCreateRequest, auth.getUsername(), postId);

        String postAuthorToken = getPostAuthorToken(postId);
        if (postAuthorToken != null)
            firebaseNotificationService.sendNotification(postAuthorToken, "새 댓글 알림", auth.getUsername() + "님이 댓글을 달았습니다.");
        return ResponseEntity.ok().build();

    }

    @Operation(summary = "댓글 전체 조회")
    @ApiResponse
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentListResponse>> getComments(
            @PathVariable("postId") Long postId) {

        return ResponseEntity.ok(commentService.findAllComments(postId));
    }

    @Operation(summary = "댓글 수정")
    @ApiResponse
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentUpdateResponse> updateComment(
        @AuthenticationPrincipal UserPrincipal auth,
        @PathVariable("postId") Long postId,
        @PathVariable("commentId") Long commentId,
        @RequestBody CommentUpdateRequest commentUpdateRequest) {

        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(
            commentService.updateComment(postId, commentId, auth.getUsername(), commentUpdateRequest));

    }


    @Operation(summary = "댓글 삭제")
    @ApiResponse
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
        @AuthenticationPrincipal UserPrincipal auth,
        @PathVariable("postId") Long postId,
        @PathVariable("commentId") Long commentId) {

        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        commentService.deleteComment(postId, commentId, auth.getUsername());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private String getPostAuthorToken(Long postId) {
        return postRepository.findById(postId)
            .map(post -> post.getUser().getFcmToken())
            .orElse(null);
    }
}