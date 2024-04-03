package codeit.api.user.handler;

import codeit.api.exception.ErrorCode;
import codeit.api.user.exception.UserException;
import codeit.api.user.transfer.UserProfileUpdatedEvent;
import codeit.common.dto.transfer.ChatUserProfileDto;
import codeit.domain.chat.repository.ChatMongoRepository;
import codeit.domain.room.repository.RoomRepository;
import codeit.domain.user.entity.User;
import codeit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserEventHandler {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ChatMongoRepository chatMongoRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserProfileUpdatedEvent(UserProfileUpdatedEvent event) {
        User user = userRepository.findById(event.getUserId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        chatMongoRepository.saveAll(chatMongoRepository.findBySenderUserId(user.getId())
                .stream().map(chat -> chat.updateChatUserProfile(user)).collect(Collectors.toList()));
        kafkaTemplate.send("chat", ChatUserProfileDto.of(user,
                roomRepository.findAllParticipatedRoomIdsByUserId(user.getId())));
    }
}
