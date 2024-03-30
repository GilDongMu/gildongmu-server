package codeit.api.comment.dto.response;

import codeit.domain.comment.entity.Comment;

public record CommentUpdateResponse(
    Long id,
    String nickname,
    String content,
    boolean secret,
    boolean owner
) {
    public static CommentUpdateResponse from(Comment comment) {
        Boolean isOwner = comment.getUser().equals(comment.getPost().getUser());
        return new CommentUpdateResponse(
            comment.getId(),
            comment.getUser().getNickname(),
            comment.getContent(),
            comment.isSecret(),
            isOwner
        );
    }
}
