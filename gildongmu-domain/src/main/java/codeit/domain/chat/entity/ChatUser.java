package codeit.domain.chat.entity;

import codeit.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatUser {
    private Long userId;
    private String nickname;
    private String profilePath;

    @Builder
    public ChatUser(Long userId, String nickname, String profilePath) {
        this.userId = userId;
        this.nickname = nickname;
        this.profilePath = profilePath;
    }

    public static ChatUser from(User user) {
        return ChatUser.builder()
                .nickname(user.getNickname())
                .profilePath(user.getProfilePath())
                .userId(user.getId()).build();
    }
}
