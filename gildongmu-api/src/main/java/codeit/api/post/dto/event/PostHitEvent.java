package codeit.api.post.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class PostHitEvent {
    private Long userId;
    private Long postId;

    public static PostHitEvent of(Long userId, Long postId){
        return PostHitEvent.builder()
                .userId(userId)
                .postId(postId).build();
    }
}
