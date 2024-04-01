package codeit.api.search.controller;

import codeit.api.post.dto.response.PostListResponse;
import codeit.api.post.service.PostService;
import codeit.api.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Validated
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final PostService postService;

    @Operation(summary = "동행 글 검색")
    @ApiResponse
    @GetMapping
    public ResponseEntity<PostListResponse> searchPosts(
        @AuthenticationPrincipal UserPrincipal auth,
        @RequestParam(required = false, value = "search") String keyword,
        @RequestParam(required = false, value = "sortby") String postSort,
        @RequestParam(required = false, value = "filter") String postFilter,
        @PageableDefault(page = 0, size = 10) Pageable pageable) {

        Pageable pageableWithoutSort = PageRequest.of(
            pageable.getPageNumber(), pageable.getPageSize(), Sort.unsorted());

        return ResponseEntity.ok(postService.findPosts(keyword, postFilter, postSort, pageableWithoutSort, auth));
    }

}
