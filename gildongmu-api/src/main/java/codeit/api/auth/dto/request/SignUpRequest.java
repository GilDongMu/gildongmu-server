package codeit.api.auth.dto.request;

import codeit.common.validator.Date;
import codeit.common.validator.EnumValue;
import codeit.domain.user.constant.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "invalid nickname")
    @Size(min = 1, max = 8, message = "invalid nickname")
    private String nickname;

    @NotBlank(message = "invalid password")
    @Size(min = 8, message = "invalid password")
    private String password;

    @Getter(AccessLevel.NONE)
    @EnumValue(enumClass = Gender.class, message = "invalid gender")
    private String gender;

    @Getter(AccessLevel.NONE)
    @Date(message = "invalid dayOfBirth")
    private String dayOfBirth;

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
}
