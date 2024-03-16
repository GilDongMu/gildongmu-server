package codeit.api.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LogInRequest {

    @Email(message = "invalid email")
    private String email;

    @NotBlank(message = "invalid blank password")
    @Size(min = 8, message = "invalid size password")
    private String password;
}
