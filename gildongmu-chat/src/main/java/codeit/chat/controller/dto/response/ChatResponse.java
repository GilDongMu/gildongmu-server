package codeit.chat.controller.dto.response;

import codeit.common.dto.transfer.ChatDto;
import codeit.common.dto.transfer.InfoChatDto;
import codeit.domain.chat.constant.ChatType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class ChatResponse implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
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

    public static ChatResponse from(InfoChatDto dto) {
        return ChatResponse.builder()
                .content(dto.getContent())
                .type(dto.getType())
                .build();
    }
}
