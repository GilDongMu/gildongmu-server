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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public void report(User complainant, ReportRequest request) {
        User target = userRepository.findById(request.getTargetUserId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Report targetReport = reportRepository.findByTarget(target)
                .orElse(Report.builder()
                        .target(target)
                        .build());
        if (targetReport.isUserAlreadyComplaint(complainant.getId()))
            throw new ReportException(ErrorCode.ALREADY_REPORTED_USER);
        targetReport.addComplaint(complainant.getId(), request.getReason());
        if (targetReport.getValidComplaintCount() >= ReportPolicy.WARING_LIMIT)
            target.classifyMaliciousUser();
        reportRepository.save(targetReport);
    }

    public Slice<ReportResponse> retrieveReports(Pageable pageable) {
        return reportRepository.findByOrderByUpdatedAtDesc(pageable)
                .map(ReportResponse::from);
    }

    @Transactional
    public void cancelToClassifyMalicious(Long userId) {
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        Report targetReport = reportRepository.findByTarget(target)
                .orElseThrow(() -> new ReportException(ErrorCode.USER_NOT_REPORTED));
        targetReport.deleteAllComplaint();
        target.cancelToClassifyMaliciousUser();
    }
}
