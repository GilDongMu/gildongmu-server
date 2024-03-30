package codeit.domain.Image.Repository;

import codeit.domain.Image.entity.Image;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<List<Image>> findAllByPostId(Long postId);

    @Query("select i.id from Image i where i.post.id = :postId")
    List<Long> findAllId(@Param("postId") Long postId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Image i where i.id in :postIds")
    void deleteAllByPost(@Param("postIds") List<Long> postIds);
}
