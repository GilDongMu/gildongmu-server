package codeit.api.auth.controller;

import codeit.api.auth.dto.request.EmailCheckRequest;
import codeit.api.auth.dto.request.LogInRequest;
import codeit.api.auth.dto.request.SignUpRequest;
import codeit.api.auth.service.AuthService;
import codeit.api.security.AuthenticationDeniedHandler;
import codeit.api.security.OAuth2LoginSuccessHandler;
import codeit.api.security.OAuth2UserServiceImpl;
import codeit.api.security.UserDetailsServiceImpl;
import codeit.domain.chat.repository.ChatMongoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private OAuth2UserServiceImpl oAuth2UserService;
    @MockBean
    private AuthenticationDeniedHandler authenticationDeniedHandler;
    @MockBean
    private AuthenticationEntryPoint authenticationEntryPoint;
    @MockBean
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;
    @MockBean
    private ChatMongoRepository chatMongoRepository;


    @Test
    @DisplayName("회원가입 성공")
    void registerTest_success() throws Exception {
        //given
        SignUpRequest request = SignUpRequest.builder()
                .email("abcde@naver.com")
                .nickname("키키")
                .password("123456789")
                .gender("FEMALE")
                .dayOfBirth("2012-01-01")
                .favoriteSpots(Set.of("다낭", "LA", "제주도"))
                .bio("안녕하세요")
                .build();
        //when
        mockMvc.perform(multipart("/auth/signup")
                        .file(new MockMultipartFile("request", "json",
                                MediaType.APPLICATION_JSON_VALUE,
                                objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)))
                        .file(new MockMultipartFile("images", "image.jpg",
                                MediaType.IMAGE_JPEG_VALUE, "abcde".getBytes()))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                ).andDo(print())
                .andExpect(status().isCreated());
        //then
    }

    @Test
    @DisplayName("로그인 성공")
    void loginTest_success() throws Exception {
        //given
        LogInRequest request = LogInRequest.builder()
                .email("abcde@naver.com")
                .password("123456789")
                .build();
        //when
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)))
                .andDo(print())
                .andExpect(status().isOk());
        //then
    }

    @Test
    @DisplayName("이메일 중복 검사 성공")
    void checkEmailTest_success() throws Exception {
        //given
        EmailCheckRequest request = EmailCheckRequest.builder()
                .email("abcde@naver.com")
                .build();
        //when
        mockMvc.perform(post("/auth/check-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)))
                .andDo(print())
                .andExpect(status().isOk());
        //then
    }

}