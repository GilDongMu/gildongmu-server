package codeit.api.contact.dto.response;

import codeit.domain.contact.entity.Contact;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ContactResponse(
        String content,
        User user,
        LocalDateTime createdAt
) {

    public static ContactResponse from(Contact contact) {
        return ContactResponse.builder()
                .createdAt(contact.getUpdatedAt())
                .content(contact.getContent())
                .user(User.from(contact.getUser()))
                .build();
    }

    @Builder
    public record User(
            Long id,
            String nickname,
            String profilePath
    ) {
        public static User from(codeit.domain.user.entity.User user) {
            return User.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .profilePath(user.getProfilePath())
                    .build();
        }
    }
}
