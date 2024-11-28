package by.ita.je.mappers;

import by.ita.je.dto.HotelDto;
import by.ita.je.models.Hotel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HotelMapper {

    private final RoomMapper roomMapper;
    private final AmenitiesMapper amenitiesMapper;
    private final ManagerMapper managerMapper;
    private final SocialMapper socialMapper;

    public HotelDto toDto(Hotel hotel) {
        if (hotel == null) return null;
        return HotelDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .rating(hotel.getRating())
                .location(hotel.getLocation())
                .description(hotel.getDescription())
                .periodOfWork(hotel.getPeriodOfWork())
                .roomsDto(hotel.getRooms() != null ? hotel.getRooms().stream().map(roomMapper::toDto).collect(Collectors.toList()) : null)
                .amenitiesDto(amenitiesMapper.toDto(hotel.getAmenities()))
                .managerDto(managerMapper.toDto(hotel.getManager()))
                .socialDto(socialMapper.toDto(hotel.getSocial()))
                .build();
    }

    public Hotel toEntity(HotelDto hotelDto) {
        if (hotelDto == null) return null;
        return Hotel.builder()
                .id(hotelDto.getId())
                .name(hotelDto.getName())
                .rating(hotelDto.getRating())
                .location(hotelDto.getLocation())
                .description(hotelDto.getDescription())
                .periodOfWork(hotelDto.getPeriodOfWork())
                .rooms(hotelDto.getRoomsDto() != null ? hotelDto.getRoomsDto().stream().map(roomMapper::toEntity).collect(Collectors.toList()) : null)
                .amenities(amenitiesMapper.toEntity(hotelDto.getAmenitiesDto()))
                .manager(managerMapper.toEntity(hotelDto.getManagerDto()))
                .social(socialMapper.toEntity(hotelDto.getSocialDto()))
                .build();
    }
}
