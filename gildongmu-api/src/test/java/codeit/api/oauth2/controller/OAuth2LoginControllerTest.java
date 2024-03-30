package codeit.api.oauth2.controller;

import codeit.api.config.WithMockOAuthLoginUser;
import codeit.api.oauth2.dto.request.OAuth2SignUpRequest;
import codeit.api.oauth2.dto.response.TokenResponse;
import codeit.api.oauth2.service.OAuth2LoginService;
import codeit.api.security.AuthenticationDeniedHandler;
import codeit.api.security.OAuth2LoginSuccessHandler;
import codeit.api.security.OAuth2UserServiceImpl;
import codeit.api.security.UserDetailsServiceImpl;
import codeit.common.security.JwtTokenManager;
import codeit.domain.chat.repository.ChatMongoRepository;
import codeit.domain.user.constant.Role;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OAuth2LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
class OAuth2LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OAuth2LoginService oAuth2LoginService;
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
    @MockBean
    private JwtTokenManager jwtTokenManager;


    @Test
    @WithMockOAuthLoginUser(role = Role.ROLE_GUEST)
    @DisplayName("OAuth2 로그인 유저의 회원가입 폼 작성 성공")
    void registerTest_success() throws Exception {
        //given
        OAuth2SignUpRequest request = OAuth2SignUpRequest.builder()
                .nickname("키키")
                .gender("FEMALE")
                .dayOfBirth("2012-01-01")
                .favoriteSpots(Set.of("다낭", "LA", "제주도"))
                .bio("안녕하세요")
                .build();
        //when
        mockMvc.perform(multipart("/oauth2/signup")
                        .file(new MockMultipartFile("request", "json",
                                MediaType.APPLICATION_JSON_VALUE,
                                objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)))
                        .file(new MockMultipartFile("images", "image.jpg",
                                MediaType.IMAGE_JPEG_VALUE, "abcde".getBytes()))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                ).andDo(print())
                .andExpect(status().is3xxRedirection());
        //then
    }

    @Test
    @WithMockOAuthLoginUser(role = Role.ROLE_GUEST)
    @DisplayName("OAuth2 로그인 유저의 정보 조회 성공")
    void loginTest_success() throws Exception {
        //given
        //when
        mockMvc.perform(get("/oauth2/signup")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        //then
    }

    @Test
    @WithMockOAuthLoginUser(role = Role.ROLE_USER)
    @DisplayName("OAuth2 로그인 유저 토큰 발급")
    void issueToken_success() throws Exception {
        //given
        given(oAuth2LoginService.issueToken(any()))
                .willReturn(TokenResponse.of("any", "any"));
        //when
        mockMvc.perform(get("/oauth2/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        //then
    }

}