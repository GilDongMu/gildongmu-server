package codeit.api.user.dto.response;

import codeit.domain.user.entity.User;
import lombok.Builder;

import java.util.List;
import java.util.Objects;


@Builder
public record UserProfileResponse(
        Long id,
        String email,
        String nickname,
        String profilePath,
        String bio,
        List<String> favoriteSpots,
        boolean isCurrentUser
) {
    public static UserProfileResponse from(User user) {
        return UserProfileResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .id(user.getId())
                .bio(user.getBio())
                .profilePath(user.getProfilePath())
                .favoriteSpots(user.getFavoriteSpots())
                .build();
    }

    public static UserProfileResponse from(User user, Long userId) {
        return UserProfileResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .id(user.getId())
                .bio(user.getBio())
                .profilePath(user.getProfilePath())
                .favoriteSpots(user.getFavoriteSpots())
                .isCurrentUser(Objects.equals(user.getId(), userId))
                .build();
    }
}
