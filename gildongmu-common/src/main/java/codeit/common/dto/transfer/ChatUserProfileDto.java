package codeit.common.dto.transfer;

import codeit.domain.user.entity.User;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserProfileDto {
    private Long userId;
    private String nickname;
    private Set<Long> roomIds;
    private String profilePath;

    public static ChatUserProfileDto of(User user, Set<Long> roomIds) {
        return ChatUserProfileDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .profilePath(user.getProfilePath())
                .roomIds(roomIds)
                .build();

    }
}
