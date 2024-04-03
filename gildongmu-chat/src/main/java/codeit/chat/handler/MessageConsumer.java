package codeit.chat.handler;

import codeit.chat.handler.dto.response.ChatResponse;
import codeit.chat.handler.dto.response.RoomEventResponse;
import codeit.common.dto.transfer.ChatDto;
import codeit.common.dto.transfer.ChatUserProfileDto;
import codeit.common.dto.transfer.InfoChatDto;
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

    @KafkaHandler
    public void handlerInfoChatEvent(InfoChatDto infoChatDto) {
        simpMessagingTemplate.convertAndSend("/rooms/" + infoChatDto.getRoomId(), ChatResponse.from(infoChatDto));
    }

    @KafkaHandler
    public void handleChatUserProfileEvent(ChatUserProfileDto chatUserProfileDto) {
        RoomEventResponse eventResponse = RoomEventResponse.from(chatUserProfileDto);
        chatUserProfileDto.getRoomIds()
                .forEach(roomId -> simpMessagingTemplate.convertAndSend("/rooms/" + roomId, eventResponse));
    }

}
