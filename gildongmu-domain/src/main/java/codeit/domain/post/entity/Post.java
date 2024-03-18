package codeit.domain.post.entity;

import codeit.domain.common.BaseTimeEntity;
import codeit.domain.place.entity.Place;
import codeit.domain.post.constant.MemberGender;
import codeit.domain.post.constant.Status;
import codeit.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "posts")
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @NotNull
    @Column(length = 100)
    private String title;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    //TODO ==> join image
    private String thumbnail;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(length = 1)
    private MemberGender memberGender;

    @NotNull
    private Short participants;

    @NotNull
    @Enumerated(EnumType.STRING)
    // TODO ==> setting Enum
    @Column(columnDefinition = "VARCHAR(20) DEFAULT '모집 중'")
    private Status status;

    /* TODO ==> tag
    @Convert(converter = StringSetConver.class)
    private Set<String> tag;
    */

    @ColumnDefault("0")
    private Long viewCount;

    /* TODO ==> join comment table
    @OneToMany
    private List<Comment> comments;
     */

}
