package codeit.api.report.service;

import codeit.api.exception.ErrorCode;
import codeit.api.report.dto.request.ReportRequest;
import codeit.api.report.dto.response.ReportResponse;
import codeit.api.report.exception.ReportException;
import codeit.api.user.exception.UserException;
import codeit.domain.report.ReportPolicy;
import codeit.domain.report.entity.Report;
import codeit.domain.report.repository.ReportRepository;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    public void report(User complainant, ReportRequest request) {
        User target = userRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Set<Long> complainants = reportRepository.findComplainantIdByTargetAndDeletedAtIsNull(target);
        if (complainants.contains(complainant.getId()))
            throw new ReportException(ErrorCode.ALREADY_REPORTED_USER);
        reportRepository.save(Report.builder()
                .reason(request.getReason())
                .complainant(complainant)
                .target(target)
                .build());
        if (complainants.size() + 1 >= ReportPolicy.WARING_LIMIT)
            target.classifyMaliciousUser();
    }

    public Slice<ReportResponse> retrieveReports() {
        Slice<Report> reportSlice = reportRepository.findByOrderByTargetId(PageRequest.of(1, 2));
        List<Report> reports = reportSlice.getContent();
        if (reports.isEmpty())
            return new SliceImpl<>(new ArrayList<>(), reportSlice.getPageable(), reportSlice.hasNext());

        Report previousReport = reports.get(0);
        List<ReportResponse> contents = new ArrayList<>();
        List<String> reasons = new ArrayList<>();
        LocalDateTime lastReportedAt = LocalDateTime.MIN;
        for (Report r : reports) {
            if (!Objects.equals(previousReport.getTarget().getId(), r.getTarget().getId())) {
                contents.add(ReportResponse.from(previousReport, reasons, lastReportedAt));
                reasons = new ArrayList<>();
                previousReport = r;
                lastReportedAt = LocalDateTime.MIN;
            }
            if (r.getCreatedAt().isAfter(lastReportedAt))
                lastReportedAt = r.getCreatedAt();
            reasons.add(r.getReason());
        }
        return new SliceImpl<>(contents, reportSlice.getPageable(), reportSlice.hasNext());
    }
}
