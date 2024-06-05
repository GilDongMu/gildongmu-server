package codeit.domain.report.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Complaint implements Serializable {
    private Long complainantId;
    private String reason;
    private Boolean isDeleted;
    private long timestamp;

    @Builder
    public Complaint(Long complainantId, String reason, boolean isDeleted, long timestamp) {
        this.complainantId = complainantId;
        this.reason = reason;
        this.isDeleted = isDeleted;
        this.timestamp = timestamp;
    }

    public static Complaint of(String reason, Long complainantId) {
        return Complaint.builder()
                .reason(reason)
                .complainantId(complainantId)
                .isDeleted(false)
                .timestamp(LocalDateTime.now().toEpochSecond(ZoneId.of("Asia/Seoul").getRules().getOffset(Instant.now())))
                .build();
    }

    public void delete() {
        isDeleted = true;
    }
}
