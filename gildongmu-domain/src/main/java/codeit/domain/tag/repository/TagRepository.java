package codeit.domain.tag.repository;

import codeit.domain.tag.entity.Tag;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findById(Long tagId);

    Optional<Tag> findByTagName(String tagName);

}
