package codeit.chat.handler.dto.response;

import codeit.chat.handler.constant.RoomEventType;
import codeit.common.dto.transfer.ChatUserProfileDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class RoomEventResponse implements Serializable {
    private RoomEventType type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ChatUserResponse sender;

    public static RoomEventResponse from(ChatUserProfileDto dto) {
        return RoomEventResponse.builder()
                .type(RoomEventType.USER_PROFILE_CHANGED)
                .sender(ChatUserResponse.from(dto))
                .build();
    }
}
