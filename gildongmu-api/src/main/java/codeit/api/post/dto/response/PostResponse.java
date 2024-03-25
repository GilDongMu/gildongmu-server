package codeit.api.post.dto.response;

import codeit.api.post.dto.TripDate;
import codeit.domain.Image.entity.Image;
import codeit.domain.post.entity.Post;

import codeit.domain.tag.entity.Tag;
import java.util.List;
import java.util.stream.Collectors;

public record PostResponse(
        Long id,
        String title,
        String nickname,
        String destination,
        List<TripDate> tripDate,
        Short numberOfPeople,
        String gender,
        String content,
        String status,
        List<String> tag,
        ImageResponse thumbnail,
        List<ImageResponse> images,
        Long countOfComments,
        Long countOfBookmarks
) {

    public static PostResponse from(Post post, List<Tag> tag, List<Image> images) {
        Image thumbnail = findThumbnail(post.getThumbnail(), images);
        List<String> tagNames = findTagNames(tag);

        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getUser().getNickname(),
                post.getDestination(),
                TripDate.toList(post.getStartDate(), post.getEndDate()),
                post.getParticipants(),
                post.getMemberGender().toString(),
                post.getContent(),
                post.getStatus().getCode(),
                tagNames,
                ImageResponse.from(thumbnail),
                ImageResponse.toList(post.getImages()),
                (long) post.getComments().size(),
                post.getBookmarkCount()
        );
    }

    private static Image findThumbnail(String url, List<Image> images) {
        return images.stream()
            .filter(image -> image.getUrl().equals(url))
            .findFirst()
            .orElse(null);
    }

    private static List<String> findTagNames(List<Tag> tags) {
        return tags.stream()
            .map(Tag::getTagName)
            .collect(Collectors.toList());
    }
}
