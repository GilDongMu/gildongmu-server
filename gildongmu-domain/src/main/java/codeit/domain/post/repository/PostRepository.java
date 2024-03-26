package codeit.domain.post.repository;

import codeit.domain.post.entity.Post;
import codeit.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Optional<Post> findById(Long postId);

    Slice<Post> findByUserOrderByStatusDesc(User user, Pageable pageable);

    @Query("select p from Post p left join Participant u on p.id = u.post.id where u.user.id = ?1 and u.status != 'DELETED' and u.isLeader = false order by p.status desc")
    Slice<Post> findByParticipantUserOrderByStatusDesc(Long userId, Pageable pageable);

}
