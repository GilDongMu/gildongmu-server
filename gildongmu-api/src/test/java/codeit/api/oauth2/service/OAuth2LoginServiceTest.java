package codeit.api.oauth2.service;

import ch.qos.logback.core.util.TimeUtil;
import codeit.api.exception.ErrorCode;
import codeit.api.oauth2.dto.request.OAuth2SignUpRequest;
import codeit.api.oauth2.dto.response.TokenResponse;
import codeit.api.oauth2.exception.OAuth2Exception;
import codeit.api.security.OAuth2LoginUser;
import codeit.common.client.S3Client;
import codeit.common.security.JwtTokenManager;
import codeit.domain.user.constant.Gender;
import codeit.domain.user.constant.Role;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuth2LoginServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenManager jwtTokenManager;
    @Mock
    private S3Client s3Client;
    @InjectMocks
    private OAuth2LoginService oAuth2LoginService;

    static User user = User.builder()
            .email("userA@google.com")
            .nickname("a-a")
            .role(Role.ROLE_GUEST)
            .build();
    MultipartFile profile = new MockMultipartFile("images", "image.jpg",
            MediaType.IMAGE_JPEG_VALUE, "abcde".getBytes());
    static MockedStatic<RequestContextHolder> contextHolderMockedStatic = mockStatic(RequestContextHolder.class);
    static OAuth2LoginUser oAuth2LoginUser = mock(OAuth2LoginUser.class);
    static ServletRequestAttributes servletRequestAttributes = mock(ServletRequestAttributes.class);
    static HttpServletRequest servletRequest = new MockHttpServletRequest();
    @BeforeAll
    static void init() {
        given(oAuth2LoginUser.getUser()).willReturn(user);
        contextHolderMockedStatic.when(RequestContextHolder::currentRequestAttributes)
                .thenReturn(servletRequestAttributes);
        given(servletRequestAttributes.getRequest()).willReturn(servletRequest);
    }

    @AfterAll
    static void tearDown(){
        contextHolderMockedStatic.close();
    }

    @Test
    @DisplayName("회원가입 성공")
    void registerTest_success() {
        //given
        User savedUser = User.builder()
                .email("userA@google.com")
                .nickname("a-a")
                .role(Role.ROLE_GUEST)
                .build();

        OAuth2SignUpRequest request = OAuth2SignUpRequest.builder()
                .nickname("키키")
                .gender("FEMALE")
                .dayOfBirth("2012-01-01")
                .favoriteSpots(Set.of("다낭", "LA", "제주도"))
                .bio("안녕하세요")
                .build();
        given(userRepository.findById(any())).willReturn(Optional.of(savedUser));
        //when
        oAuth2LoginService.register(oAuth2LoginUser, request, profile);
        //then
        verify(s3Client, times(1)).upload(any());
        assertEquals(Role.ROLE_USER, savedUser.getRole());
        assertEquals("userA@google.com", savedUser.getEmail());
        assertEquals(Gender.FEMALE, savedUser.getGender());
        assertEquals(LocalDate.parse("2012-01-01"), savedUser.getDateOfBirth());
        assertEquals("안녕하세요", savedUser.getBio());
        assertEquals("키키", savedUser.getNickname());
        assertEquals(3, savedUser.getFavoriteSpots().size());
    }

    @Test
    @DisplayName("회원가입 성공")
    void registerTest_successWhenMultipartFileIsNull() {
        //given
        User savedUser = User.builder()
                .email("userA@google.com")
                .nickname("a-a")
                .role(Role.ROLE_GUEST)
                .build();

        OAuth2SignUpRequest request = OAuth2SignUpRequest.builder()
                .nickname("키키")
                .gender("FEMALE")
                .dayOfBirth("2012-01-01")
                .favoriteSpots(Set.of("다낭", "LA", "제주도"))
                .bio("안녕하세요")
                .build();
        given(userRepository.findById(any())).willReturn(Optional.of(savedUser));
        //when
        oAuth2LoginService.register(oAuth2LoginUser, request, null);
        //then
        verify(s3Client, times(0)).upload(any());
        assertEquals(Role.ROLE_USER, savedUser.getRole());
        assertEquals("userA@google.com", savedUser.getEmail());
        assertEquals(Gender.FEMALE, savedUser.getGender());
        assertNull(savedUser.getProfilePath());
        assertEquals(LocalDate.parse("2012-01-01"), savedUser.getDateOfBirth());
        assertEquals("안녕하세요", savedUser.getBio());
        assertEquals("키키", savedUser.getNickname());
        assertEquals(3, savedUser.getFavoriteSpots().size());
    }

    @Test
    @DisplayName("회원가입 실패-USER_NOT_FOUND")
    void registerTest_fail_USER_NOT_FOUND() {
        //given
        User savedUser = User.builder()
                .email("userA@google.com")
                .nickname("a-a")
                .role(Role.ROLE_GUEST)
                .build();

        OAuth2SignUpRequest request = OAuth2SignUpRequest.builder()
                .nickname("키키")
                .gender("FEMALE")
                .dayOfBirth("2012-01-01")
                .favoriteSpots(Set.of("다낭", "LA", "제주도"))
                .bio("안녕하세요")
                .build();
        given(userRepository.findById(any())).willReturn(Optional.empty());
        //when
        OAuth2Exception e = assertThrows(OAuth2Exception.class,
                () -> oAuth2LoginService.register(oAuth2LoginUser, request, profile));
        //then
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("토큰 발급 성공")
    void issueTokenTest_success() {
        //given
        given(jwtTokenManager.generateAccessToken(anyString()))
                .willReturn("accessTokenValue");
        given(jwtTokenManager.generateRefreshToken(anyString()))
                .willReturn("refreshTokenValue");

        OAuth2LoginUser oAuth2LoginUser = new OAuth2LoginUser(new DefaultOAuth2User(new ArrayList<>(), Map.of("any", "any"), "any"), "any", User.builder()
                .role(Role.ROLE_USER)
                .email("abcde@gmail.com")
                .build());

        //when
        TokenResponse response = oAuth2LoginService.issueToken(oAuth2LoginUser);
        //then
        assertEquals("accessTokenValue", response.accessToken());
        assertEquals("refreshTokenValue", response.refreshToken());
    }
}