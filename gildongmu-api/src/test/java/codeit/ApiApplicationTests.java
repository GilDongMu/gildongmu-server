package codeit;

import codeit.common.client.S3Client;
import codeit.common.config.KafkaConsumerConfig;
import codeit.common.config.KafkaProducerConfig;
import codeit.common.config.S3Config;
import codeit.common.security.JwtTokenManager;
import codeit.domain.post.repository.PostRepository;
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
	PostRepository postRepository;
	@MockBean
	KafkaTemplate<String, Object> kafkaTemplate;
	@MockBean
	KafkaConsumerConfig kafkaConsumerConfig;
	@MockBean
	KafkaProducerConfig kafkaProducerConfig;
	@MockBean
	S3Config s3Config;
	@MockBean
	S3Client s3Client;

	@Test
	void contextLoads() {
	}

}
