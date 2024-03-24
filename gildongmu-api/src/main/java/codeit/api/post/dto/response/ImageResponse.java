package codeit.api.post.dto.response;

import codeit.domain.Image.entity.Image;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record ImageResponse(
    Long id,
    String url
) {
    public static ImageResponse from(Image image) {
        if (image == null) return null;
        return ImageResponse.builder()
            .id(image.getId())
            .url(image.getUrl())
            .build();
    }

    public static List<ImageResponse> toList(List<Image> images) {
        if (images == null) return null;
        return images.stream()
            .map(ImageResponse::from)
            .collect(Collectors.toList());
    }
}
