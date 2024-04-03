package codeit.chat.handler.dto.response;

import codeit.common.dto.transfer.ChatDto;
import codeit.common.dto.transfer.ChatUserProfileDto;
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

    public static ChatUserResponse from(ChatUserProfileDto dto) {
        return ChatUserResponse.builder()
                .id(dto.getUserId())
                .nickname(dto.getNickname())
                .profilePath(dto.getProfilePath())
                .build();
    }
}
