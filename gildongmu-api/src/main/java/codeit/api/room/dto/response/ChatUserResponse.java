package codeit.api.room.dto.response;

import codeit.domain.chat.entity.ChatUser;
import lombok.Builder;

import java.util.Objects;


@Builder
public record ChatUserResponse(
        Long id,
        String nickname,
        String profilePath,
        boolean isCurrentUser
) {
    public static ChatUserResponse from(ChatUser chatUser, Long currentUserId) {
        return ChatUserResponse.builder()
                .nickname(chatUser.getNickname())
                .id(chatUser.getUserId())
                .profilePath(chatUser.getProfilePath())
                .isCurrentUser(Objects.equals(currentUserId, chatUser.getUserId()))
                .build();
    }
}
