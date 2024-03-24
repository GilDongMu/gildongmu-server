package codeit.api.post.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ImageCreateRequest(
    @NotEmpty
    String url,

    @NotNull
    boolean thumbnail
) {

}
