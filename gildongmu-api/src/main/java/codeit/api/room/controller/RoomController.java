package codeit.api.room.controller;

import codeit.api.room.dto.response.ChatResponse;
import codeit.api.room.dto.response.RoomInfoResponse;
import codeit.api.room.dto.response.RoomResponse;
import codeit.api.room.service.RoomService;
import codeit.api.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;

    @Operation(summary = "소통공간 채팅 리스트 조회")
    @ApiResponse
    @GetMapping("/{roomId}/chats")
    private ResponseEntity<Slice<ChatResponse>> retrieveChats(
            @PathVariable Long roomId, @PageableDefault(page = 0, size = 10, sort = "created_at", direction = DESC) Pageable pageable, @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(roomService.retrieveChats(principal.getUser(), roomId, pageable));
    }

    @Operation(summary = "내가 속한 소통공간 조회")
    @ApiResponse
    @GetMapping
    private ResponseEntity<Slice<RoomResponse>> retrieveRooms(@PageableDefault(page = 0, size = 10) Pageable pageable,
                                                              @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(roomService.retrieveRooms(principal.getUser(), pageable));
    }

    @Operation(summary = "소통공간 정보 조회")
    @ApiResponse
    @GetMapping("/{roomId}")
    private ResponseEntity<RoomInfoResponse> retrieveRoom(@PathVariable Long roomId,
                                                          @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(roomService.retrieveRoom(principal.getUser(), roomId));
    }
}
