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
    private final SocialMapper socialMapper;
    private final ProfileMapper profileMapper;

    public HotelDto toDto(Hotel hotel) {
        if (hotel == null) {return null;}
        return HotelDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .location(hotel.getLocation())
                .description(hotel.getDescription())
                .periodOfWork(hotel.getPeriodOfWork())
                .roomsDto(hotel.getRooms().stream()
                        .map(roomMapper::toDto)
                        .collect(Collectors.toList()))
                .amenitiesDto(amenitiesMapper.toDto(hotel.getAmenities()))
                .socialDto(socialMapper.toDto(hotel.getSocial()))
                .profilesDto(hotel.getProfiles().stream()
                        .map(profileMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public Hotel toEntity(HotelDto dto) {
        if (dto == null) {return null;}
        return Hotel.builder()
                .id(dto.getId())
                .name(dto.getName())
                .location(dto.getLocation())
                .description(dto.getDescription())
                .periodOfWork(dto.getPeriodOfWork())
                .rooms(dto.getRoomsDto().stream()
                        .map(roomMapper::toEntity)
                        .collect(Collectors.toList()))
                .amenities(amenitiesMapper.toEntity(dto.getAmenitiesDto()))
                .social(socialMapper.toEntity(dto.getSocialDto()))
                .profiles(dto.getProfilesDto().stream()
                        .map(profileMapper::toEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
