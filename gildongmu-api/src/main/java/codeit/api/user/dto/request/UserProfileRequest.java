package codeit.api.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRequest {
    @NotBlank(message = "invalid blank nickname")
    @Size(min = 1, max = 8, message = "invalid size nickname")
    private String nickname;

    private boolean isPasswordChanged;

    @NotBlank(message = "invalid blank password")
    @Size(min = 8, message = "invalid size password")
    private String password;

    @Getter(AccessLevel.NONE)
    @Size(max = 3, message = "invalid favoriteSpots")
    @Builder.Default
    private Set<String> favoriteSpots = new HashSet<>();

    @Size(max = 200, message = "invalid bio")
    private String bio;

    public List<String> getFavoriteSpots() {
        return new ArrayList<>(favoriteSpots);
    }
}
