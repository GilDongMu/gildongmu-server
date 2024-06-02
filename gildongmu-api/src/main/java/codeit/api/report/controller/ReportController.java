package codeit.api.report.controller;

import codeit.api.report.dto.request.ReportRequest;
import codeit.api.report.dto.response.ReportResponse;
import codeit.api.report.service.ReportService;
import codeit.api.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    @PostMapping
    public ResponseEntity<Void> report(@RequestBody @Valid ReportRequest request,
                                       @AuthenticationPrincipal UserPrincipal principal) {
        reportService.report(principal.getUser(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Slice<ReportResponse>> retrieveReports(){
        return ResponseEntity.ok(reportService.retrieveReports());
    }
}
