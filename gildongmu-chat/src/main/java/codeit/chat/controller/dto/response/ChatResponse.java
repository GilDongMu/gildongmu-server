package codeit.chat.controller.dto.response;

import codeit.domain.chat.constant.ChatType;
import codeit.domain.chat.entity.Chat;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class ChatResponse implements Serializable {
    private String id;
    private ChatUserResponse sender;
    private String content;
    private ChatType type;

    public static ChatResponse from(Chat chat) {
        return ChatResponse.builder()
                .id(chat.getId())
                .content(chat.getContent())
                .type(chat.getType())
                .sender(ChatUserResponse.from(chat.getSender()))
                .build();
    }
}
