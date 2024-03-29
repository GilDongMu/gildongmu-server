package codeit.api.post.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record TripDate(
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate
) {

    public static TripDate of(LocalDate startDate, LocalDate endDate) {
        return TripDate.builder()
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }
/*
    public static List<TripDate> toList(LocalDate startDate, LocalDate endDate) {
        return List.of(TripDate.builder()
            .startDate(startDate)
            .endDate(endDate)
            .build());
    }
 */
}
