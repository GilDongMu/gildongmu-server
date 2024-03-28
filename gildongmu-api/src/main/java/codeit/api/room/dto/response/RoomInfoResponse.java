package codeit.api.room.dto.response;

import codeit.domain.room.entity.Room;
import lombok.Builder;


@Builder
public record RoomInfoResponse(
        Long id,
        Integer headCount,
        String thumbnail,
        String title
) {
    public static RoomInfoResponse from(Room room) {
        return RoomInfoResponse.builder()
                .id(room.getId())
                .headCount(room.getHeadcount())
                .thumbnail(room.getPost().getThumbnail())
                .title(room.getPost().getTitle())
                .build();
    }
}
