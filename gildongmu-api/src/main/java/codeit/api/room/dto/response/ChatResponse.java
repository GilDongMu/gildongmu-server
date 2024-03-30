package codeit.api.room.dto.response;

import codeit.domain.chat.constant.ChatType;
import codeit.domain.chat.entity.Chat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatResponse(
        String id,
        String content,
        LocalDateTime createdAt,
        ChatType type,
        ChatUserResponse sender
) {
    public static ChatResponse from(Chat chat, Long currentUserId) {
        if (chat.getType().isInfoType())
            return ofInfoChat(chat);
        return ofUserChat(chat, currentUserId);
    }

    private static ChatResponse ofUserChat(Chat chat, Long currentUserId) {
        return ChatResponse.builder()
                .id(chat.getId())
                .content(chat.getContent())
                .createdAt(chat.getCreatedAt())
                .type(chat.getType())
                .sender(ChatUserResponse.from(chat.getSender(), currentUserId))
                .build();
    }

    private static ChatResponse ofInfoChat(Chat chat) {
        return ChatResponse.builder()
                .content(chat.getContent())
                .type(chat.getType())
                .build();
    }
}
