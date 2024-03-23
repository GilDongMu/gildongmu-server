package codeit.chat.controller.dto.response;

import codeit.domain.chat.constant.ChatType;
import codeit.domain.chat.entity.Chat;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class ChatMessageResponse implements Serializable {
    private String id;
    private ChatUserResponse sender;
    private String content;
    private ChatType type;

    public static ChatMessageResponse from(Chat chat) {
        return ChatMessageResponse.builder()
                .id(chat.getId())
                .content(chat.getContent())
                .type(ChatType.MESSAGE)
                .sender(ChatUserResponse.from(chat.getSender()))
                .build();
    }
}
