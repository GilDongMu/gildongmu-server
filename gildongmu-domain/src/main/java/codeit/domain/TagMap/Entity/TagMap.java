package codeit.domain.TagMap.Entity;

import codeit.domain.common.BaseTimeEntity;
import codeit.domain.post.entity.Post;
import codeit.domain.tag.entity.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tag_map")
public class TagMap extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_map_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Builder
    public TagMap(Post post, Tag tag) {
        this.post = post;
        this.tag = tag;
    }

    public void deletePost(Post post) {
        this.post = post;
    }

}
