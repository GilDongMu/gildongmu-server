package codeit.chat.controller.dto.response;

import codeit.chat.handler.dto.transfer.ChatDto;
import codeit.domain.chat.constant.ChatType;
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

    public static ChatResponse from(ChatDto dto) {
        return ChatResponse.builder()
                .id(dto.getChatId())
                .content(dto.getContent())
                .type(dto.getType())
                .sender(ChatUserResponse.from(dto.getSender()))
                .build();
    }
}
