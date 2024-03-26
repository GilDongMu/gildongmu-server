package codeit.api.participant.controller;

import codeit.api.config.WithMockCustomUser;
import codeit.api.participant.service.ParticipantService;
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
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ParticipantController.class)
@AutoConfigureMockMvc(addFilters = false)
class ParticipantControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
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
    @MockBean
    private ParticipantService participantService;

    @Test
    @WithMockCustomUser(role = Role.ROLE_USER)
    @DisplayName("여행글 신청 성공")
    void applyForParticipantTest_success() throws Exception {
        //given
        //when
        mockMvc.perform(post("/posts/{postId}/participants", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        //then
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_USER)
    @DisplayName("여행글 신청 취소 및 참여 취소 성공")
    void exitParticipantTest_success() throws Exception {
        //given
        //when
        mockMvc.perform(delete("/posts/{postId}/participants", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_USER)
    @DisplayName("신청자 거절 및 추방")
    void denyParticipantTest_success() throws Exception {
        //given
        //when
        mockMvc.perform(delete("/posts/{postId}/participants/{participantId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_USER)
    @DisplayName("신청자 수락")
    void acceptParticipantTest_success() throws Exception {
        //given
        //when
        mockMvc.perform(put("/posts/{postId}/participants/{participantId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockCustomUser(role = Role.ROLE_USER)
    @DisplayName("참여자 목록 조회 성공")
    void retrieveParticipantsTest_success() throws Exception {
        //given
        //when
        mockMvc.perform(get("/posts/{postId}/participants", 1L)
                        .param("status", "PENDING")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        //then
    }


}