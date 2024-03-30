package codeit.chat.controller.dto.response;

import codeit.chat.handler.dto.transfer.ChatDto;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatUserResponse {
    private Long id;
    private String nickname;
    private String profilePath;

    public static ChatUserResponse from(ChatDto.Sender sender) {
        return ChatUserResponse.builder()
                .id(sender.getId())
                .nickname(sender.getNickname())
                .profilePath(sender.getProfilePath())
                .build();
    }
}
