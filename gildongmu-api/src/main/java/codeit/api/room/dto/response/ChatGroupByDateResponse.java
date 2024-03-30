package codeit.api.room.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ChatGroupByDateResponse(
        @JsonFormat(pattern = "yyyy년 M월 d일 (E)", locale = "ko")
        LocalDate date,
        @ArraySchema(schema = @Schema(description = "채팅들"))
        List<ChatResponse> chats
) {
        public static ChatGroupByDateResponse of(LocalDate date, List<ChatResponse> chats){
                return ChatGroupByDateResponse.builder()
                        .date(date)
                        .chats(chats)
                        .build();
        }
}
