package codeit.api.user.controller;

import codeit.api.config.WithMockCustomUser;
import codeit.api.security.AuthenticationDeniedHandler;
import codeit.api.security.OAuth2LoginSuccessHandler;
import codeit.api.security.OAuth2UserServiceImpl;
import codeit.api.security.UserDetailsServiceImpl;
import codeit.api.user.dto.request.PasswordCheckRequest;
import codeit.api.user.dto.request.UserProfileRequest;
import codeit.api.user.service.UserService;
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
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
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
    @WithMockCustomUser(role = Role.ROLE_USER)
    @DisplayName("회원 정보 수정 성공")
    void modifyProfileTest_success() throws Exception {
        //given
        UserProfileRequest request = UserProfileRequest.builder()
                .bio("안녕")
                .isPasswordChanged(true)
                .favoriteSpots(Set.of("괌"))
                .password("changed-password")
                .nickname("라이언")
                .build();
        //when
        mockMvc.perform(multipart("/users/me")
                        .file(new MockMultipartFile("request", "json",
                                MediaType.APPLICATION_JSON_VALUE,
                                objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(httpServletRequest -> {
                            httpServletRequest.setMethod("PUT");
                            return httpServletRequest;
                        })
                ).andDo(print())
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_USER)
    @DisplayName("회원 정보 조회 성공")
    void retrieveMyProfileTest_success() throws Exception {
        //given
        //when
        mockMvc.perform(get("/users/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_USER)
    @DisplayName("비밀번호 검증 성공")
    void checkMyPasswordTest_success() throws Exception {
        //given
        PasswordCheckRequest request = new PasswordCheckRequest("my-password");
        //when
        mockMvc.perform(get("/users/me/check-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8)))
                .andDo(print())
                .andExpect(status().isOk());
        //then
    }


}