package codeit.api.bookmark.controller;

import codeit.api.bookmark.service.BookmarkService;
import codeit.api.post.dto.PostItem;
import codeit.api.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @Operation(summary = "찜 생성")
    @ApiResponse
    @PostMapping("/posts/{postId}/bookmarks")
    public ResponseEntity<Void> createBookmark(
            @AuthenticationPrincipal UserPrincipal auth,
            @PathVariable("postId") Long postId) {

        bookmarkService.createBookmark(auth.getUsername(), postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "찜 삭제")
    @ApiResponse
    @DeleteMapping("/posts/{postId}/bookmarks")
    public ResponseEntity<Void> deleteBookmark(
            @AuthenticationPrincipal UserPrincipal auth,
            @PathVariable("postId") Long postId) {

        bookmarkService.deleteBookmark(auth.getUsername(), postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "찜 목록")
    @ApiResponse
    @GetMapping("/bookmarks")
    public ResponseEntity<List<PostItem>> getBookmarks(
            @AuthenticationPrincipal UserPrincipal auth) {
        return ResponseEntity.ok(bookmarkService.findBookmarks(auth.getUsername()));
    }
}
