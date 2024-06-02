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
import org.springframework.data.domain.Pageable;
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

    public Slice<ReportResponse> retrieveReports(Pageable pageable) {
        Slice<Report> reportSlice = reportRepository.findByOrderByCreatedAtDescTargetId(pageable);
        List<ReportResponse> contents = getSortedGroupByTargetUserReportResponse(reportSlice.getContent());
        return new SliceImpl<>(contents, reportSlice.getPageable(), reportSlice.hasNext());
    }

    private List<ReportResponse> getSortedGroupByTargetUserReportResponse(List<Report> reports){
        if (reports.isEmpty()) return new ArrayList<>();

        List<ReportResponse> contents = new ArrayList<>();

        Report prev = reports.get(0);
        List<String> reasons = new ArrayList<>();
        LocalDateTime lastReportedAt = prev.getCreatedAt();
        for (Report r: reports ){
            if(!Objects.equals(prev.getTarget().getId(), r.getTarget().getId())){
                contents.add(ReportResponse.of(prev, reasons, lastReportedAt));
                reasons = new ArrayList<>();
                prev = r;
                lastReportedAt = r.getCreatedAt();
            }
            reasons.add(r.getReason());
        }
        return contents;
    }
}
