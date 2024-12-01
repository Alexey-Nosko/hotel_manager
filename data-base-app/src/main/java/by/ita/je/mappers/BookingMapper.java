package by.ita.je.mappers;


import by.ita.je.dto.BookingDto;
import by.ita.je.models.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final ProfileMapper profileMapper;

    public BookingDto toDto(Booking booking) {
        if (booking == null) {return null;}
        return BookingDto.builder()
                .id(booking.getId())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .profileDto(profileMapper.toDto(booking.getProfile()))
                .build();
    }

    public Booking toEntity(BookingDto dto) {
        if (dto == null) {return null;}
        return Booking.builder()
                .id(dto.getId())
                .checkInDate(dto.getCheckInDate())
                .checkOutDate(dto.getCheckOutDate())
                .profile(profileMapper.toEntity(dto.getProfileDto()))
                .build();
    }
}