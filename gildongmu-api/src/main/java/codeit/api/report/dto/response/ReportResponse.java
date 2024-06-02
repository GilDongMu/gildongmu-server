package codeit.api.report.dto.response;

import codeit.domain.report.entity.Report;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReportResponse(
        int countOfReports,
        List<String> reasons,
        User user,
        LocalDateTime lastReportedAt
) {

    public static ReportResponse from(Report report, List<String> reasons, LocalDateTime lastReportedAt){
        return ReportResponse.builder()
                .countOfReports(reasons.size())
                .lastReportedAt(lastReportedAt)
                .reasons(reasons)
                .user(User.from(report.getTarget()))
                .build();
    }

    @Builder
    public record User(
            Long id,
            String nickname,
            String profilePath,
            boolean isCurrentUser
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
