package by.ita.je.mappers;

import by.ita.je.dto.BookingDto;
import by.ita.je.models.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final ClientMapper clientMapper;

    public BookingDto toDto(Booking booking) {
        if (booking == null) return null;
        return BookingDto.builder()
                .id(booking.getId())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .clientDto(clientMapper.toDto(booking.getClient()))
                .build();
    }

    public Booking toEntity(BookingDto bookingDto) {
        if (bookingDto == null) return null;
        return Booking.builder()
                .id(bookingDto.getId())
                .checkInDate(bookingDto.getCheckInDate())
                .checkOutDate(bookingDto.getCheckOutDate())
                .client(clientMapper.toEntity(bookingDto.getClientDto()))
                .build();
    }
}