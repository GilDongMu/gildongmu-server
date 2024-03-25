package codeit.api.post.dto.request;

import codeit.api.post.dto.TripDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PostUpdateRequest(
    @NotBlank(message = "Title is required.")
    String title,

    @NotBlank(message = "Destination is required.")
    String destination,

    @NotNull(message = "Trip Date cannot be null")
    TripDate tripDate,

    Short numberOfPeople,

    @NotNull(message = "Member Gender cannot be null")
    String gender,

    @NotBlank(message = "Content is required.")
    String content,

    List<String> tag,

    List<ImageCreateRequest> images
) {

}
