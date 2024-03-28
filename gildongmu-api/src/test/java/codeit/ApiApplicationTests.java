package codeit;

import codeit.common.security.JwtTokenManager;
import codeit.domain.room.repository.RoomRepository;
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
	@MockBean
	RoomRepository roomRepository;

	@Test
	void contextLoads() {
	}

}
