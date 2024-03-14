package codeit.api.mock.dto.response;

import codeit.domain.user.entity.User;
import lombok.Builder;

@Builder
public record MockResponse(Long id,
                           String breed,
                           String age) {
    public static MockResponse from(User user) {
        return MockResponse.builder()
                .id(user.getId())
                .build();
    }

}
