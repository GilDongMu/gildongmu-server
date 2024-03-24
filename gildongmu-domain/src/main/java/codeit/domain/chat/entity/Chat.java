package codeit.domain.chat.entity;

import codeit.domain.chat.constant.ChatType;
import codeit.domain.common.BaseTimeMongoDocument;
import codeit.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "chats")
public class Chat extends BaseTimeMongoDocument {
    @Id
    private String id;
    private ChatType type;
    private Long roomId;
    private String content;
    private ChatUser sender;

    @Builder
    public Chat(ChatType type, Long roomId, String content, User user) {
        this.type = type;
        this.roomId = roomId;
        this.content = content;
        this.sender = ChatUser.from(user);
    }
}
