package codeit.api.auth.dto.request;

import codeit.common.validator.Date;
import codeit.common.validator.EnumValue;
import codeit.domain.user.constant.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "invalid blank nickname")
    @Size(min = 1, max = 8, message = "invalid size nickname")
    private String nickname;

    @NotBlank(message = "invalid blank password")
    @Size(min = 8, message = "invalid size password")
    private String password;

    @Getter(AccessLevel.NONE)
    @EnumValue(enumClass = Gender.class, message = "invalid gender")
    private String gender;

    @Getter(AccessLevel.NONE)
    @Date(message = "invalid dayOfBirth")
    private String dayOfBirth;

    @Getter(AccessLevel.NONE)
    @Builder.Default
    @Size(max = 3, message = "invalid favoriteSpots")
    private Set<String> favoriteSpots = new HashSet<>();

    @Size(max = 200, message = "invalid bio")
    private String bio;

    public Gender getGender() {
        return Gender.valueOf(this.gender);
    }

    public LocalDate getDayOfBirth() {
        return LocalDate.parse(dayOfBirth);
    }

    public List<String> getFavoriteSpots() {
        return new ArrayList<>(favoriteSpots);
    }
}
