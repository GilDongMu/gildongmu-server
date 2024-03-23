package codeit.chat.controller.dto.response;

import codeit.domain.chat.entity.ChatUser;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatUserResponse {
    private Long id;
    private String nickname;
    private String profilePath;

    public static ChatUserResponse from(ChatUser user) {
        return ChatUserResponse.builder()
                .id(user.getUserId())
                .nickname(user.getNickname())
                .profilePath(user.getProfilePath())
                .build();
    }
}
