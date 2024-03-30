package codeit.api.comment.dto.request;

public record CommentUpdateRequest(
        String content,
        boolean secret
) {

}
