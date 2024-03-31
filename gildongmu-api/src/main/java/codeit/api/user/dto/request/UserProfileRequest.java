package codeit.api.user.dto.request;

import codeit.api.exception.ErrorCode;
import codeit.api.user.exception.UserException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileRequest {
    @NotBlank(message = "invalid blank nickname")
    @Size(min = 1, max = 8, message = "invalid size nickname")
    private String nickname;

    private Boolean isPasswordChanged;

    @Getter(AccessLevel.NONE)
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

    public Optional<String> getPassword() {
        if (!isPasswordChanged) return Optional.empty();
        try {
            return Optional.of(password);
        } catch (NullPointerException e) {
            throw new UserException(ErrorCode.PASSWORD_NOT_VALID);
        }
    }
}
