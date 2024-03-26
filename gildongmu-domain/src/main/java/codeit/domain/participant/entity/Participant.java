package codeit.domain.participant.entity;

import codeit.domain.common.BaseTimeEntity;
import codeit.domain.participant.constant.Status;
import codeit.domain.post.entity.Post;
import codeit.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "participants")
public class Participant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "bit(1) default 0")
    private boolean isLeader;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public void delete() {
        this.status = Status.DELETED;
    }

    public void accept() {
        this.status = Status.ACCEPTED;
    }

    public boolean isAccepted() {
        return Status.ACCEPTED.equals(status);
    }

    @Builder
    public Participant(boolean isLeader, Status status, User user, Post post) {
        this.isLeader = isLeader;
        this.status = status;
        this.user = user;
        this.post = post;
    }
}
