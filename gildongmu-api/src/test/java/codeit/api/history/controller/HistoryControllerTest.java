package codeit.api.history.controller;

import codeit.api.config.WithMockCustomUser;
import codeit.api.history.service.HistoryService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HistoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class HistoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private HistoryService historyService;
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
    @DisplayName("조회 내역 조회 성공")
    void retrieveChatsTest_success() throws Exception {
        //given
        //when
        mockMvc.perform(get("/histories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        //then
    }

}