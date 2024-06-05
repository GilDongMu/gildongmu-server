package codeit.domain.report.entity;

import codeit.domain.common.BaseTimeEntity;
import codeit.domain.user.entity.User;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reports", indexes = {
        @Index(name = "idx_target_id", columnList = "target_id")
})
@Slf4j
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private User target;

    @Type(JsonType.class)
    @Column(name = "complaints", columnDefinition = "json")
    private List<Complaint> complaints = new ArrayList<>();

    @Transient
    @Getter(AccessLevel.NONE)
    private List<Complaint> validComplaints = new ArrayList<>();

    @Builder
    public Report(User target) {
        this.target = target;
    }

    public void addComplaint(Long complainantId, String reason) {
        complaints.add(Complaint.of(reason, complainantId));
    }

    private List<Complaint> getValidComplaints() {
        if (validComplaints.isEmpty()) {
            this.validComplaints = complaints.stream()
                    .filter(complaint -> !complaint.getIsDeleted()).collect(Collectors.toList());
        }
        return validComplaints;
    }

    public List<String> getReasons() {
        return getValidComplaints().stream().map(Complaint::getReason).collect(Collectors.toList());
    }

    public int getValidComplaintCount() {
        return getValidComplaints().size();
    }

    private Set<Long> getValidComplainantIds() {
        return getValidComplaints().stream()
                .map(Complaint::getComplainantId).collect(Collectors.toSet());
    }

    public boolean isUserAlreadyComplaint(Long userId) {
        return getValidComplainantIds().contains(userId);
    }

    public void deleteAllComplaint() {
        getComplaints().forEach(Complaint::delete);
    }
}
