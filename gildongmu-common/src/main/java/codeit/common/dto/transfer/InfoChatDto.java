package codeit.common.dto.transfer;

import codeit.domain.chat.constant.ChatType;
import codeit.domain.chat.entity.Chat;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoChatDto {
    private String content;
    private ChatType type;
    private Long roomId;

    public static InfoChatDto from(Chat chat) {
        return InfoChatDto.builder()
                .content(chat.getContent())
                .type(chat.getType())
                .roomId(chat.getRoomId()).build();

    }
}
