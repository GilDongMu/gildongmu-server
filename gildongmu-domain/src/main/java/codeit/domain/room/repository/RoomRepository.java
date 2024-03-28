package codeit.domain.room.repository;

import codeit.domain.post.entity.Post;
import codeit.domain.room.entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByPost(Post post);

    Optional<Room> findByPostId(Long postId);

    @EntityGraph(attributePaths = {"post"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("select r from Room r left join fetch r.post p " +
            "left join Participant pa on p.id = pa.post.id " +
            "where pa.user.id = ?1 " +
            "order by abs(function('DATEDIFF', p.startDate, current_date)) ")
    Slice<Room> findParticipatedRoomByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"post"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query("select r from Room r left join fetch r.post p " +
            "left join Participant pa on p.id = pa.post.id " +
            "where r.id = ?1 and pa.user.id = ?2 ")
    Optional<Room> findParticipatedRoomById(Long id, Long userId);
}
