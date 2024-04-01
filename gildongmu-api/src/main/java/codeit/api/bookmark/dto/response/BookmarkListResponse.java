package codeit.api.bookmark.dto.response;

import codeit.api.post.dto.PostItem;
import codeit.api.post.dto.TripDate;
import java.util.List;

public record BookmarkListResponse(
    Long id,
    String title,
    String nickname,
    String destination,
    TripDate tripDate,
    Short numberOfPeople,
    String gender,
    String content,
    String status,
    List<String> tag,
    String thumbnail,
    Long countOfComments,
    Long countOfBookmarks,
    boolean myBookmark,
    boolean myPost
) {
    public static BookmarkListResponse from(PostItem postItem, boolean myPost) {
        return new BookmarkListResponse(
            postItem.id(),
            postItem.title(),
            postItem.nickname(),
            postItem.destination(),
            postItem.tripDate(),
            postItem.numberOfPeople(),
            postItem.gender(),
            postItem.content(),
            postItem.status(),
            postItem.tag(),
            postItem.thumbnail(),
            postItem.countOfComments(),
            postItem.countOfBookmarks(),
            postItem.myBookmark(),
            myPost
        );
    }
}
