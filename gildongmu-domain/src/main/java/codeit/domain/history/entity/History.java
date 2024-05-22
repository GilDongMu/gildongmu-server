package codeit.domain.history.entity;

import codeit.domain.history.HistoryPolicy;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@RedisHash(value = "post-histories")
public class History implements Serializable {
    @Id
    private Long userId;

    @Column
    @Builder.Default
    private List<Long> postIds = new ArrayList<>();

    public void addPostId(Long postId) {
        postIds.remove(postId);
        postIds.add(0, postId);
        while (postIds.size() > HistoryPolicy.MAX_HISTORY_SIZE) {
            postIds.remove(postIds.size() - 1);
        }
    }

    public void removeDeletedPostIds(List<Long> savedPostIds) {
        postIds.retainAll(savedPostIds);
    }
}
