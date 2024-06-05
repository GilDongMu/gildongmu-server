package codeit.api.report.dto.response;

import codeit.domain.report.entity.Report;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReportResponse(
        int countOfComplaints,
        List<String> reasons,
        User user,
        LocalDateTime lastReportedAt
) {

    public static ReportResponse from(Report report) {
        return ReportResponse.builder()
                .countOfComplaints(report.getValidComplaintCount())
                .lastReportedAt(report.getUpdatedAt())
                .reasons(report.getReasons())
                .user(User.from(report.getTarget()))
                .build();
    }

    @Builder
    public record User(
            Long id,
            String nickname,
            String profilePath
    ) {
        public static User from(codeit.domain.user.entity.User user) {
            return User.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profilePath(user.getProfilePath())
                    .build();
        }
    }
}
