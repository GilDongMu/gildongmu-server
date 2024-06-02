package codeit.api.report.controller;

import codeit.api.report.dto.request.ReportRequest;
import codeit.api.report.dto.response.ReportResponse;
import codeit.api.report.service.ReportService;
import codeit.api.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    @Operation(summary = "유저 신고하기")
    @ApiResponse
    @PostMapping
    public ResponseEntity<Void> report(@RequestBody @Valid ReportRequest request,
                                       @AuthenticationPrincipal UserPrincipal principal) {
        reportService.report(principal.getUser(), request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "신고 목록 조회")
    @ApiResponse
    @GetMapping
    public ResponseEntity<Slice<ReportResponse>> retrieveReports(@PageableDefault(page = 0, size = 10) Pageable pageable){
        return ResponseEntity.ok(reportService.retrieveReports(pageable));
    }
}
