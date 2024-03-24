package codeit.domain.TagMap.repository;

import codeit.domain.TagMap.Entity.TagMap;
import codeit.domain.post.entity.Post;
import codeit.domain.tag.entity.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagMapRepository extends JpaRepository<TagMap, Long> {

    List<TagMap> findAllByPost(Post post);
}
