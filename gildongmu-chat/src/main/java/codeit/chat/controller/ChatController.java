package codeit.chat.controller;

import codeit.chat.controller.dto.request.ChatImageRequest;
import codeit.chat.controller.dto.request.ChatMessageRequest;
import codeit.chat.controller.dto.response.ChatMessageResponse;
import codeit.chat.security.UserPrincipal;
import codeit.chat.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/rooms/{roomId}/message")
    @SendTo("/rooms/{roomId}")
    public ChatMessageResponse message(@DestinationVariable Long roomId, @Valid @Payload ChatMessageRequest message, UserPrincipal principal) {
        return chatService.message(roomId, message, principal.getUser());
    }

    @MessageMapping("/rooms/{roomId}/image")
    @SendTo("/rooms/{roomId}")
    public ChatImageRequest image(@DestinationVariable Long roomId, @Valid @Payload ChatImageRequest message, UserPrincipal principal) {
        return message;
    }
}
