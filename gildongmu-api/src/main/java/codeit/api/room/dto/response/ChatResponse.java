package codeit.api.room.dto.response;

import codeit.domain.chat.constant.ChatType;
import codeit.domain.chat.entity.Chat;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record ChatResponse(
        String id,
        String content,
        LocalDateTime createdAt,
        boolean isMessageType,
        ChatUserResponse sender
) {
    public static ChatResponse from(Chat chat, Long currentUserId) {
        return ChatResponse.builder()
                .id(chat.getId())
                .content(chat.getContent())
                .createdAt(chat.getCreatedAt())
                .isMessageType(ChatType.MESSAGE.equals(chat.getType()))
                .sender(ChatUserResponse.from(chat.getSender(), currentUserId))
                .build();
    }
}
