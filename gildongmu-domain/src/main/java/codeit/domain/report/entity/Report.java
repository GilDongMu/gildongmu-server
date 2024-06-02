package codeit.domain.report.entity;

import codeit.domain.common.BaseTimeEntity;
import codeit.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reports", indexes = {
        @Index(name = "idx_target_id", columnList = "target_id")
})
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private User target;

    @Column(columnDefinition = "blob", nullable = false)
    private String reason;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "complainant_id", nullable = false)
    private User complainant;

    private LocalDateTime deletedAt;

    @Builder
    public Report(User target, String reason, User complainant, LocalDateTime deletedAt) {
        this.target = target;
        this.reason = reason;
        this.complainant = complainant;
        this.deletedAt = deletedAt;
    }
}
