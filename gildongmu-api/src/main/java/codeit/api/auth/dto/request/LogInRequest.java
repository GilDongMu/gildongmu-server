package codeit.api.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LogInRequest {

    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "invalid password")
    @Size(min = 8, message = "invalid password")
    private String password;
}
