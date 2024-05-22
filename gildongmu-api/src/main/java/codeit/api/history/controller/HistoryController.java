package codeit.api.history.controller;

import codeit.api.history.service.HistoryService;
import codeit.api.post.dto.PostItem;
import codeit.api.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/histories")
public class HistoryController {
    private final HistoryService historyService;

    @Operation(summary = "동행글 히스토리 조회")
    @ApiResponse
    @GetMapping
    public ResponseEntity<List<PostItem>> retrievePostHistory(@AuthenticationPrincipal UserPrincipal principal){
        return ResponseEntity.ok(historyService.retrieveHistory(principal.getUser()));
    }
}
