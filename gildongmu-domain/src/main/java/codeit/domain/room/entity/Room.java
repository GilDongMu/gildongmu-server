package codeit.domain.room.entity;

import codeit.domain.common.BaseTimeEntity;
import codeit.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "rooms")
public class Room extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime lastChatAt;

    private int headcount;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    public Room(LocalDateTime lastChatAt, Integer headcount, Post post) {
        this.lastChatAt = lastChatAt;
        this.headcount = headcount;
        this.post = post;
    }

    public void plusHeadCount() {
        headcount++;
    }

    public void minusHeadCount() {
        headcount--;
    }
}
