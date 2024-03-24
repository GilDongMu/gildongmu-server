package codeit.chat.security;

import codeit.chat.exception.ErrorCode;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserPrincipalService {
    private final UserRepository userRepository;

    protected void loadUserPrincipal(UserPrincipal principal, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new MessageDeliveryException(ErrorCode.USER_NOT_FOUND.name()));
        principal.loadUser(user);
    }
}
