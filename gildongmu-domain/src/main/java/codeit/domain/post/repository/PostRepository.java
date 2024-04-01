package codeit.domain.post.repository;

import codeit.domain.post.constant.Status;
import codeit.domain.post.entity.Post;
import codeit.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    @EntityGraph(attributePaths = {"user"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Post> findById(Long postId);

    Slice<Post> findByUserOrderByStatusDesc(User user, Pageable pageable);

    @Query("select p from Post p left join Participant u on p.id = u.post.id where u.user.id = ?1 and u.status != 'DELETED' and u.isLeader = false order by p.status desc")
    Slice<Post> findByParticipantUserOrderByStatusDesc(Long userId, Pageable pageable);

    Optional<Post> findByIdAndStatus(Long postId, Status status);

    @Query("select p from Post p "
        + "left join p.bookmarks b "
        + "left join p.comments c "
        + "where (:keyword is null or p.destination like %:keyword%) "
        + "and (:filter is null "
        + "or (:filter = 'female' and p.memberGender = 'FEMALE') "
        + "or (:filter = 'male' and p.memberGender = 'MALE') "
        + "or (:filter = 'none' and p.memberGender = 'NONE') "
        + "or (:filter = 'open' and p.status = 'OPEN') "
        + "or (:filter = 'close' and p.status = 'CLOSE')) "
        + "group by p "
        + "order by "
        + "case :sortby "
        + "when 'popular' then count(b.id) "
        + "when 'comment' then count(c.id) "
        + "when 'latest' then p.updatedAt end desc, "
        + "case :sortby when 'trip' then abs(datediff(current_date(), p.startDate)) end asc")
    Page<Post> findFilteredAndSortedPosts(
        @Param("keyword") String keyword, @Param("filter") String filter,
        @Param("sortby") String sort, Pageable pageable);
}