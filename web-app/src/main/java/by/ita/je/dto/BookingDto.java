package by.ita.je.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private UUID id;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private ProfileDto profileDto;
}
