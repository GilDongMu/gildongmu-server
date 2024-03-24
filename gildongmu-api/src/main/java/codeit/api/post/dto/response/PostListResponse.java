package codeit.api.post.dto.response;

import codeit.api.post.dto.TripDate;
import codeit.domain.post.entity.Post;

import java.security.PrivateKey;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PostListResponse(
    List<PostListItemResponse> content,
    Pageable pageable,
    boolean first,
    boolean last,
    int size,
    int number,
    Sort sort,
    int numberOfElements,
    boolean empty

) {
    public record PostListItemResponse(
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
        String thumbnail,
        Long countOfComments,
        Long countOfBookmarks
    ) {
    }
}