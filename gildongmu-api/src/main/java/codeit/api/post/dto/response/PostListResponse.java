package codeit.api.post.dto.response;

import codeit.api.post.dto.PostItem;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public record PostListResponse(
    List<PostItem> content,
    Pageable pageable,
    boolean first,
    boolean last,
    int size,
    int number,
    Sort sort,
    int numberOfElements,
    boolean empty,
    int totalPages

) {
}