package codeit.api.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordCheckRequest {

    @NotBlank(message = "invalid blank password")
    @Size(min = 8, message = "invalid size password")
    private String password;
}
