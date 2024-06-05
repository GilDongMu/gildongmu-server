package codeit.api.contact.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest {
    @NotBlank(message = "invalid blank content")
    @Size(min = 5, message = "content must be at least 5")
    private String content;
}
