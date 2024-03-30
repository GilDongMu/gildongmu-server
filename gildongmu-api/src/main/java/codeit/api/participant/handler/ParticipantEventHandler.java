package codeit.api.participant.handler;

import codeit.api.exception.ErrorCode;
import codeit.api.participant.dto.transfer.ParticipantAcceptedEvent;
import codeit.api.user.exception.UserException;
import codeit.common.dto.transfer.InfoChatDto;
import codeit.domain.chat.constant.ChatInfoPolicy;
import codeit.domain.chat.constant.ChatType;
import codeit.domain.chat.entity.Chat;
import codeit.domain.chat.repository.ChatMongoRepository;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ParticipantEventHandler {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ChatMongoRepository chatMongoRepository;
    private final UserRepository userRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleParticipantAcceptedEvent(ParticipantAcceptedEvent event) {
        User user = userRepository.findById(event.userId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        kafkaTemplate.send("chat", InfoChatDto.from(chatMongoRepository.save(Chat.builder()
                .roomId(event.roomId())
                .content(ChatInfoPolicy.PARTICIPANT_ENTER_INFO.getInfoMessage(user.getNickname()))
                .type(ChatType.INFO)
                .build())));
    }
}
