package codeit.api.user.service;

import codeit.api.exception.ErrorCode;
import codeit.api.user.dto.request.PasswordCheckRequest;
import codeit.api.user.dto.request.UserProfileRequest;
import codeit.api.user.dto.response.PasswordCheckResponse;
import codeit.api.user.dto.response.UserProfileResponse;
import codeit.api.user.exception.UserException;
import codeit.domain.user.constant.Role;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    static User userA = User.builder()
            .favoriteSpots(List.of("다낭", "제주도"))
            .email("userA@google.com")
            .nickname("a")
            .bio("안녕하세요")
            .profilePath("/profile-path")
            .role(Role.ROLE_USER)
            .password("encoded")
            .build();

    static User user;

    MultipartFile profile = new MockMultipartFile("images", "image.jpg",
            MediaType.IMAGE_JPEG_VALUE, "abcde".getBytes());

    @BeforeAll
    static void init() {
        user = mock(User.class);
    }

    @Test
    @DisplayName("나의 정보 조회 성공")
    void retrieveMyProfileTest_success() {
        //given
        //when
        UserProfileResponse response = userService.retrieveMyProfile(userA);
        //then
        assertEquals(2, response.favoriteSpots().size());
        assertEquals("a", response.nickname());
        assertEquals("userA@google.com", response.email());
        assertEquals("/profile-path", response.profilePath());
        assertEquals("안녕하세요", response.bio());
    }

    @Test
    @DisplayName("나의 정보 수정 성공")
    void modifyProfileTest_success() {
        //given
        UserProfileRequest request = UserProfileRequest.builder()
                .bio("안녕")
                .isPasswordChanged(true)
                .favoriteSpots(Set.of("괌"))
                .password("changed-password")
                .nickname("라이언")
                .build();
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.of(userA));
        given(passwordEncoder.encode(anyString())).willReturn("encoded-password");
        //when
        userService.modifyProfile(request, profile, user);
        //then
        assertEquals("안녕", userA.getBio());
        assertEquals("encoded-password", userA.getPassword());
        assertEquals("라이언", userA.getNickname());
        assertEquals("괌", userA.getFavoriteSpots().get(0));
    }

    @Test
    @DisplayName("나의 정보 수정 실패-USER_NOT_FOUND")
    void modifyProfileTest_fail_USER_NOT_FOUND() {
        //given
        UserProfileRequest request = UserProfileRequest.builder()
                .bio("안녕")
                .isPasswordChanged(true)
                .favoriteSpots(Set.of("괌"))
                .password("changed-password")
                .nickname("라이언")
                .build();
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        //when
        UserException e = assertThrows(UserException.class,
                () -> userService.modifyProfile(request, profile, user));
        //then
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("비밀번호 체크 성공")
    void checkMyPasswordTest_success() {
        //given
        PasswordCheckRequest request = new PasswordCheckRequest("my-password");
        given(passwordEncoder.matches(anyString(), anyString()))
                .willReturn(true);
        //when
        PasswordCheckResponse response = userService.checkMyPassword(request, userA);
        //then
        assertTrue(response.isCorrect());
    }


}