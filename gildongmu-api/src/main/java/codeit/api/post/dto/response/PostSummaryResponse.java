package codeit.api.post.dto.response;


import codeit.domain.post.entity.Post;
import lombok.Builder;

import java.util.Objects;

@Builder
public record PostSummaryResponse(
        Long id,
        String title,
        String content,
        String status,
        int numberOfCapacity,
        int numberOfAccepted,
        User user
) {

    public static PostSummaryResponse from(Post post, int numberOfAccepted, Long currentUserId) {
        return PostSummaryResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .status(post.getStatus().name())
                .content(post.getContent())
                .numberOfAccepted(numberOfAccepted)
                .numberOfCapacity(post.getParticipants())
                .user(User.from(post.getUser(), currentUserId))
                .build();
    }

    @Builder
    public record User(
            Long id,
            String nickname,
            String profilePath,
            boolean isMalicious,
            boolean isDeleted,
            boolean isCurrentUser
    ) {
        public static User from(codeit.domain.user.entity.User user, Long currentUserId) {
            return User.builder()
                    .id(user.getId())
                    .isMalicious(user.isMalicious())
                    .nickname(user.getNickname())
                    .profilePath(user.getProfilePath())
                    .isDeleted(user.isDeleted())
                    .isCurrentUser(Objects.equals(user.getId(), currentUserId))
                    .build();
        }
    }

}
