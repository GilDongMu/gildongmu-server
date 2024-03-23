package codeit.chat.security;

import codeit.chat.exception.ErrorCode;
import codeit.common.security.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompMessageInterceptor implements ChannelInterceptor {
    private final UserPrincipalService userPrincipalService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand()))
            authenticate(accessor);

        return message;
    }


    private void authenticate(StompHeaderAccessor accessor) {
        String token = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
        if (ObjectUtils.isEmpty(token) || !JwtTokenManager.validate(token))
            throw new MessageDeliveryException(ErrorCode.NOT_AUTHENTICATED_USER.name());
        Optional.ofNullable((UserPrincipal) accessor.getUser())
                .ifPresent(principal -> userPrincipalService.loadUserPrincipal(principal, JwtTokenManager.parseEmail(token)));
    }
}
