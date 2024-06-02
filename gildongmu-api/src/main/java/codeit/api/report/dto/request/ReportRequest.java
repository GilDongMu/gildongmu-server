package codeit.api.report.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest {
    @NotBlank(message = "invalid blank reason")
    @Size(min = 5, message = "reason must be at least 5")
    private String reason;

    @NotNull
    private Long targetUserId;
}
