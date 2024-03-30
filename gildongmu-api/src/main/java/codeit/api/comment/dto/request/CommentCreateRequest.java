package codeit.api.comment.dto.request;

public record CommentCreateRequest(
        String content,
        boolean secret,
        Long parentId
) {

}
