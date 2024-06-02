package codeit.api.user.dto.response;

import codeit.domain.user.constant.Gender;
import codeit.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
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
        boolean isMalicious,
        @JsonInclude(JsonInclude.Include.NON_NULL) Gender gender,
        List<String> favoriteSpots,
        @JsonInclude(JsonInclude.Include.NON_NULL) Boolean isCurrentUser
) {
    public static UserProfileResponse from(User user) {
        return UserProfileResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .id(user.getId())
                .bio(user.getBio())
                .profilePath(user.getProfilePath())
                .favoriteSpots(user.getFavoriteSpots())
                .isMalicious(user.isMalicious())
                .build();
    }

    public static UserProfileResponse from(User user, Long userId) {
        return UserProfileResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .id(user.getId())
                .bio(user.getBio())
                .profilePath(user.getProfilePath())
                .favoriteSpots(user.getFavoriteSpots())
                .isCurrentUser(Objects.equals(user.getId(), userId))
                .isMalicious(user.isMalicious())
                .build();
    }
}
