package codeit;

import codeit.common.security.JwtTokenManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ChatApplicationTests {
    @MockBean
    JwtTokenManager jwtTokenManager;
    @Test
    void contextLoads() {
    }

}
