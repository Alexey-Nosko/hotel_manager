package by.ita.je.mappers;

import by.ita.je.dto.RoomDto;
import by.ita.je.models.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoomMapper {

    private final BookingMapper bookingMapper;

    public RoomDto toDto(Room room) {
        if (room == null) {return null;}
        return RoomDto.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .type(room.getType())
                .pricePerNight(room.getPricePerNight())
                .isAvailable(room.getIsAvailable())
                .bookingsDto(room.getBookings().stream()
                        .map(bookingMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public Room toEntity(RoomDto dto) {
        if (dto == null) {return null;}
        return Room.builder()
                .id(dto.getId())
                .roomNumber(dto.getRoomNumber())
                .type(dto.getType())
                .pricePerNight(dto.getPricePerNight())
                .isAvailable(dto.getIsAvailable())
                .bookings(dto.getBookingsDto().stream()
                        .map(bookingMapper::toEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}