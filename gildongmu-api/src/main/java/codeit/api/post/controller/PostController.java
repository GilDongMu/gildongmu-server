package codeit.api.post.controller;

import codeit.api.post.dto.request.PostCreateRequest;
import codeit.api.post.dto.request.PostUpdateRequest;
import codeit.api.post.dto.response.PostListResponse;
import codeit.api.post.dto.response.PostResponse;
import codeit.api.post.service.PostService;
import codeit.common.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    @ApiResponse
    @Operation(description = "동행 글 전체 조회.", summary = "동행 목록 조회")
    public ResponseEntity<PostListResponse> getPosts(
            @RequestParam(required = false, defaultValue = "latest", value = "sort") String postSort,
            @RequestParam(required = false, value = "filter") String postFilter,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
            ) {

        Sort sort = postService.getSort(postSort);
        Pageable pageable = PageRequest.of(page, size, sort);

        PostListResponse posts  = postService.findPosts(postFilter, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{postId}")
    @ApiResponse
    @Operation(summary = "동행 단일 글 조회")
    public ResponseEntity<PostResponse> getPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.findPost(postId));
    }

    @Operation(summary = "동행 글 생성")
    @ApiResponse
    @PostMapping
    public ResponseEntity<Void> createPost(
            @AuthenticationPrincipal UserPrincipal auth,
            @RequestBody @Valid PostCreateRequest postCreateRequest) {

        postService.createPost(postCreateRequest, auth.getUsername());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "동행 글 수정")
    @ApiResponse
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
        @AuthenticationPrincipal UserPrincipal auth,
        @PathVariable("postId") Long postId,
        @RequestBody @Valid PostUpdateRequest postUpdateRequest) {

        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PostResponse result = postService.findPost(postId);
        if (!result.nickname().equals(auth.getNickname())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(postService.updatePost(postId, postUpdateRequest));
    }

    @Operation(summary = "동행 글 삭제")
    @ApiResponse
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
        @AuthenticationPrincipal UserPrincipal auth,
        @PathVariable Long postId) {

        if (auth == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PostResponse result = postService.findPost(postId);
        if (!result.nickname().equals(auth.getNickname())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        postService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}