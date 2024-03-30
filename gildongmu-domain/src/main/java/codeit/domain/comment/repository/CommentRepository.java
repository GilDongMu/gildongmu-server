package codeit.domain.comment.repository;

import codeit.domain.comment.entity.Comment;
import codeit.domain.post.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByIdAscCreatedAtAsc(Long postId);

    @Query("select c from Comment c left join fetch c.parent " +
        "where c.post = :post " +
        "order by c.parent.id asc nulls first, c.createdAt asc")
    List<Comment> findAllByPost(@Param("post") Post post);
}
