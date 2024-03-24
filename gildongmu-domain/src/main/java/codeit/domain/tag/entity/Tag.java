package codeit.domain.tag.entity;

import codeit.domain.TagMap.Entity.TagMap;
import codeit.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Tags")
public class Tag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @NotNull
    private String tagName;

    //@OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<TagMap> tagMaps;

    @Builder
    public Tag(String tagName) {
        this.tagName = tagName;
    }
}
