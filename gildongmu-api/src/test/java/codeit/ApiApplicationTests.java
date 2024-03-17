package codeit;

import codeit.api.config.SecurityConfig;
import codeit.common.security.JwtTokenManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@SpringBootTest
class ApiApplicationTests {

	@MockBean
	JwtTokenManager jwtTokenManager;
	@MockBean
	ClientRegistrationRepository clientRegistrationRepository;

	@Test
	void contextLoads() {
	}

}
