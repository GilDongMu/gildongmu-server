package codeit.chat.handler;

import codeit.chat.controller.dto.response.ChatResponse;
import codeit.chat.handler.dto.transfer.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@KafkaListener(topics = "chat", groupId = "group-chat")
public class MessageConsumer {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @KafkaHandler
    public void handlerSendChatEvent(ChatDto chatDto) {
        simpMessagingTemplate.convertAndSend("/rooms/" + chatDto.getRoomId(), ChatResponse.from(chatDto));
    }

}
