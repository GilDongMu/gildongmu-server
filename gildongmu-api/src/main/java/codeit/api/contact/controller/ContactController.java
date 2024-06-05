package codeit.api.contact.controller;

import codeit.api.contact.dto.request.ContactRequest;
import codeit.api.contact.dto.response.ContactResponse;
import codeit.api.contact.service.ContactService;
import codeit.api.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contacts")
public class ContactController {
    private final ContactService contactService;

    @Operation(summary = "문의하기")
    @ApiResponse
    @PostMapping
    public ResponseEntity<Void> report(@RequestBody @Valid ContactRequest request,
                                       @AuthenticationPrincipal UserPrincipal principal) {
        contactService.create(request, principal.getUser());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "문의 목록 조회")
    @ApiResponse
    @GetMapping
    public ResponseEntity<Slice<ContactResponse>> retrieveReports(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(contactService.retrieve(pageable));
    }
}
