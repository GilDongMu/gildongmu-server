package codeit.domain.bookmark.repository;

import codeit.domain.bookmark.entity.Bookmark;
import codeit.domain.post.entity.Post;
import codeit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserAndPost(User user, Post post);

    void deleteByUserAndPost(User user, Post post);

    List<Bookmark> findByUser(User user);

    Long countByPost(Post post);


}
