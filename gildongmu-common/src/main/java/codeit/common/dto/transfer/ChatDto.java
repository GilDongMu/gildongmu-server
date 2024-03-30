package codeit.common.dto.transfer;

import codeit.domain.chat.constant.ChatType;
import codeit.domain.chat.entity.Chat;
import codeit.domain.chat.entity.ChatUser;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    private String chatId;
    private String content;
    private ChatType type;
    private Long roomId;
    private Sender sender;

    public static ChatDto of(Chat chat) {
        return ChatDto.builder()
                .chatId(chat.getId())
                .content(chat.getContent())
                .type(chat.getType())
                .sender(Sender.from(chat.getSender()))
                .roomId(chat.getRoomId()).build();

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sender {
        private Long id;
        private String nickname;
        private String profilePath;

        public static Sender from(ChatUser chatUser) {
            return Sender.builder()
                    .id(chatUser.getUserId())
                    .profilePath(chatUser.getProfilePath())
                    .nickname(chatUser.getNickname())
                    .build();
        }
    }
}
