package codeit.api.post.service;

import codeit.domain.TagMap.Entity.TagMap;
import codeit.domain.TagMap.repository.TagMapRepository;
import codeit.domain.post.entity.Post;
import codeit.domain.tag.entity.Tag;
import codeit.domain.tag.repository.TagRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapRepository tagMapRepository;

    public void saveTag(Post post, List<String> tagNames) {

        if (tagNames.size() == 0) return;

        tagNames.stream().map(tag -> tagRepository.findByTagName(tag)
            .orElseGet(() -> tagRepository.save(
                Tag.builder()
                    .tagName(tag).build())))
            .forEach(tag -> mapTagToPost(post, tag));
    }

    private Long mapTagToPost(Post post, Tag tag) {
        return tagMapRepository.save(
            TagMap.builder()
                .post(post)
                .tag(tag)
                .build()
        ).getId();
    }

    public List<Tag> findTagListByPost(Post post) {
        List<Tag> tags = tagMapRepository.findAllByPost(post).stream()
            .map(TagMap::getTag)
            .collect(Collectors.toList());
        return tags;
    }

    public void deleteTag(Post post) {
        List<TagMap> tagMaps = tagMapRepository.findAllByPost(post);
        for (TagMap tagMap : tagMaps) {
            tagMap.deletePost(null);
        }
    }
}
