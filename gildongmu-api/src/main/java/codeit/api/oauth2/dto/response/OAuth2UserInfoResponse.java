package codeit.api.oauth2.dto.response;

import codeit.domain.user.entity.User;
import lombok.Builder;

@Builder
public record OAuth2UserInfoResponse(String email) {
    public static OAuth2UserInfoResponse from(User user) {
        return OAuth2UserInfoResponse.builder()
                .email(user.getEmail())
                .build();
    }
}
