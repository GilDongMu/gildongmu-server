package codeit.domain.room.entity;

import codeit.domain.common.BaseTimeEntity;
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

    private Integer headcount;

    // TODO: add post

    @Builder
    public Room(LocalDateTime lastChatAt, Integer headcount) {
        this.lastChatAt = lastChatAt;
        this.headcount = headcount;
    }
}
