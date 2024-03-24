package codeit.chat.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatImageRequest {
    @NotBlank(message = "imageByte not blank")
    private String imageByte;
}
