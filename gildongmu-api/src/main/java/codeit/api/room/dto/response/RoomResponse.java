package codeit.api.room.dto.response;

import codeit.domain.room.entity.Room;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record RoomResponse(
        Long id,
        String lastChatMessage,
        LocalDateTime lastChatAt,
        Integer headCount,
        String thumbnail,
        String title
) {
    public static RoomResponse from(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .lastChatAt(room.getPost().getCreatedAt())
                .headCount(room.getHeadcount())
                .thumbnail(room.getPost().getThumbnail())
                .title(room.getPost().getTitle())
                .build();
    }
}
