package by.ita.je.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    private UUID id;

    private Integer roomNumber;

    private String type;

    private Integer pricePerNight;

    private Boolean isAvailable;

    private List<Booking> bookings;
}
