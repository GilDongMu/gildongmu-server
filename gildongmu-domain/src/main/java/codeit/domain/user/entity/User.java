package codeit.domain.user.entity;

import codeit.domain.common.BaseTimeEntity;
import codeit.domain.user.constant.Gender;
import codeit.domain.user.constant.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    @Column(nullable = false, columnDefinition = "varchar(10)")
    private Role role;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, columnDefinition = "varchar(8)")
    private String nickname;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(6)")
    private Gender gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    private String profilePath;

    @Column(columnDefinition = "varchar(200)")
    private String bio;

    @Column(name = "favorite_spots", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> favoriteSpots = new ArrayList<>();
}
