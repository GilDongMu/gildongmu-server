package codeit;

import codeit.common.config.KafkaConsumerConfig;
import codeit.common.config.KafkaProducerConfig;
import codeit.common.security.JwtTokenManager;
import codeit.domain.room.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@SpringBootTest
class ApiApplicationTests {

	@MockBean
	JwtTokenManager jwtTokenManager;
	@MockBean
	ClientRegistrationRepository clientRegistrationRepository;
	@MockBean
	RoomRepository roomRepository;
	@MockBean
	KafkaTemplate<String, Object> kafkaTemplate;
	@MockBean
	KafkaConsumerConfig kafkaConsumerConfig;
	@MockBean
	KafkaProducerConfig kafkaProducerConfig;

	@Test
	void contextLoads() {
	}

}
