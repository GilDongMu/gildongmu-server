package codeit.domain.post.entity;

import codeit.domain.Bookmark.entity.Bookmark;
import codeit.domain.Image.entity.Image;
import codeit.domain.TagMap.Entity.TagMap;
import codeit.domain.comment.entity.Comment;
import codeit.domain.common.BaseTimeEntity;
import codeit.domain.post.constant.MemberGender;
import codeit.domain.post.constant.Status;
import codeit.domain.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import lombok.AccessLevel;
import lombok.Builder;
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

    @NotNull
    @Column
    private String destination;

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

    private String thumbnail;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Image> images;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(columnDefinition = "VARCHAR(6) DEFAULT 'NONE'")
    private MemberGender memberGender;

    @NotNull
    private Short participants;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'OPEN'")
    private Status status;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Bookmark> bookmarks;

    @ColumnDefault("0")
    private Long viewCount;

    @ColumnDefault("0")
    private Long bookmarkCount;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    //@OrderBy("asc")
    private List<Comment> comments;


    public void add(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Builder(toBuilder = true)
    public Post(User user, String destination, String title, String content,
                LocalDate startDate, LocalDate endDate, String thumbnail, List<Image> images,
                MemberGender memberGender, Short participants, Status status,
                Set<Bookmark> bookmarks, Long viewCount, Long bookmarkCount, List<Comment> comments) {
        this.user = user;
        this.destination = destination;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnail = thumbnail;
        this.images = images;
        this.memberGender = memberGender;
        this.participants = participants;
        this.status = status;
        this.bookmarks = bookmarks;
        this.viewCount = viewCount;
        this.bookmarkCount = bookmarkCount;
        this.comments = comments;
    }

    public void updateDestination(String destination) {
        this.destination =destination;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void updateEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void updateThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void updateImages(List<Image> images) {
        this.images = images;
    }

    public void updateGender(MemberGender memberGender) {
        this.memberGender = memberGender;
    }

    public void updateParticipants(Short participants) {
        this.participants = participants;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}
