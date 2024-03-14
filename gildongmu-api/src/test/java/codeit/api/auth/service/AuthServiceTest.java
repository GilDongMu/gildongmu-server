package codeit.api.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import codeit.api.auth.dto.request.LogInRequest;
import codeit.api.auth.dto.request.SignUpRequest;
import codeit.api.auth.dto.response.EmailCheckResponse;
import codeit.api.auth.dto.response.TokenResponse;
import codeit.api.auth.exception.AuthException;
import codeit.api.exception.ErrorCode;
import codeit.common.security.JwtTokenManager;
import codeit.common.security.dto.TokenDto;
import codeit.domain.user.constant.Gender;
import codeit.domain.user.constant.Role;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenManager jwtTokenManager;
    @InjectMocks
    private AuthService authService;

    User userA = User.builder()
        .email("userA@google.com")
        .nickname("a-a")
        .role(Role.ROLE_USER)
        .password("encoded")
        .build();
    MultipartFile profile = new MockMultipartFile("images", "image.jpg",
        MediaType.IMAGE_JPEG_VALUE, "abcde".getBytes());

    @Test
    @DisplayName("회원가입 성공")
    void registerTest_success() {
        //given
        SignUpRequest request = SignUpRequest.builder()
            .email("abcde@naver.com")
            .nickname("키키")
            .password("1234")
            .gender("FEMALE")
            .dayOfBirth("2012-01-01")
            .favoriteSpots(Set.of("다낭", "LA", "제주도"))
            .bio("안녕하세요")
            .build();
        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("encoded-password");
        //when
        authService.register(request, profile);
        //then
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(captor.capture());
        User savedUser = captor.getValue();
        assertEquals(Role.ROLE_USER, savedUser.getRole());
        assertEquals("abcde@naver.com", savedUser.getEmail());
        assertEquals(Gender.FEMALE, savedUser.getGender());
        assertEquals(LocalDate.parse("2012-01-01"), savedUser.getDateOfBirth());
        assertEquals("안녕하세요", savedUser.getBio());
        assertEquals("키키", savedUser.getNickname());
        assertEquals("encoded-password", savedUser.getPassword());
        assertEquals(3, savedUser.getFavoriteSpots().size());
    }

    @Test
    @DisplayName("회원가입 실패-ALREADY_REGISTERED_EMAIL")
    void registerTest_fail_ALREADY_REGISTERED_EMAIL() {
        //given
        SignUpRequest request = SignUpRequest.builder()
            .email("abcde@naver.com")
            .nickname("키키")
            .password("1234")
            .gender("FEMALE")
            .dayOfBirth("2012-01-01")
            .favoriteSpots(Set.of("다낭", "LA", "제주도"))
            .bio("안녕하세요")
            .build();
        given(userRepository.existsByEmail(anyString())).willReturn(true);
        //when
        AuthException e = assertThrows(AuthException.class,
            () -> authService.register(request, profile));
        //then
        assertEquals(ErrorCode.ALREADY_REGISTERED_EMAIL, e.getErrorCode());
    }

    @Test
    @DisplayName("로그인 성공")
    void loginTest_success() {
        //given
        LogInRequest request = LogInRequest.builder()
            .email("abcde@naver.com")
            .password("1234")
            .build();
        given(userRepository.findByEmail(anyString()))
            .willReturn(Optional.of(userA));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(jwtTokenManager.generate(anyString()))
            .willReturn(TokenDto.builder()
                .accessToken("accessTokenValue")
                .refreshToken("refreshTokenValue")
                .build());
        //when
        TokenResponse response = authService.login(request);
        //then
        assertEquals("accessTokenValue", response.accessToken());
        assertEquals("refreshTokenValue", response.refreshToken());
    }

    @Test
    @DisplayName("로그인 실패-USER_NOT_FOUND")
    void loginTest_fail_USER_NOT_FOUND() {
        //given
        LogInRequest request = LogInRequest.builder()
            .email("abcde@naver.com")
            .password("1234")
            .build();
        //when
        AuthException e = assertThrows(AuthException.class,
            () -> authService.login(request));
        //then
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("로그인 실패-PASSWORD_UNMATCHED")
    void loginTest_fail_PASSWORD_UNMATCHED() {
        //given
        LogInRequest request = LogInRequest.builder()
            .email("abcde@naver.com")
            .password("1234")
            .build();
        given(userRepository.findByEmail(anyString()))
            .willReturn(Optional.of(userA));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);
        //when
        AuthException e = assertThrows(AuthException.class,
            () -> authService.login(request));
        //then
        assertEquals(ErrorCode.PASSWORD_UNMATCHED, e.getErrorCode());
    }

    @Test
    @DisplayName("이메일 중복 검사 성공")
    void checkEmail_success() {
        //given
        given(userRepository.existsByEmail(anyString())).willReturn(false);
        //when
        EmailCheckResponse response = authService.checkEmail("abcde@gmail.com");
        //then
        assertTrue(response.isUsable());
    }

    @Test
    @DisplayName("이메일 중복 검사 성공")
    void checkEmail_success_GivenRegisteredEmail() {
        //given
        given(userRepository.existsByEmail(anyString())).willReturn(true);
        //when
        EmailCheckResponse response = authService.checkEmail("abcde@gmail.com");
        //then
        assertFalse(response.isUsable());
    }

}