package codeit.domain.user.entity;

import codeit.domain.common.BaseTimeEntity;
import codeit.domain.user.constant.Gender;
import codeit.domain.user.constant.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 8)
    private String nickname;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(length = 6)
    private Gender gender;

    private LocalDate dateOfBirth;

    private String profilePath;

    @Column(length = 200)
    private String bio;

    @Column(name = "favorite_spots", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> favoriteSpots = new ArrayList<>();

    public void registerOAuth2User(String nickname, Gender gender, LocalDate dateOfBirth,
                                   String bio, List<String> favoriteSpots, String profilePath) {
        this.nickname = nickname;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;
        this.favoriteSpots = favoriteSpots;
        this.role = Role.ROLE_USER;
        this.profilePath = profilePath;
    }

    public void update(String nickname, String bio, List<String> favoriteSpots, String profilePath) {
        this.nickname = nickname;
        this.bio = bio;
        this.favoriteSpots = favoriteSpots;
        this.role = Role.ROLE_USER;
        this.profilePath = profilePath;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    @Builder
    public User(Role role, String email, String nickname, String password, Gender gender,
                LocalDate dateOfBirth, String profilePath, String bio, List<String> favoriteSpots) {
        this.role = role;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.profilePath = profilePath;
        this.bio = bio;
        this.favoriteSpots = favoriteSpots;
    }
}
