package by.ita.je.mappers;

import by.ita.je.dto.AmenitiesDto;
import by.ita.je.models.Amenities;
import org.springframework.stereotype.Component;

@Component
public class AmenitiesMapper {

    public AmenitiesDto toDto(Amenities amenities) {
        if (amenities == null) {return null;}
        return AmenitiesDto.builder()
                .id(amenities.getId())
                .wifi(amenities.getWifi())
                .pool(amenities.getPool())
                .airConditioner(amenities.getAirConditioner())
                .parking(amenities.getParking())
                .build();
    }

    public Amenities toEntity(AmenitiesDto dto) {
        if (dto == null) {return null;}
        return Amenities.builder()
                .id(dto.getId())
                .wifi(dto.getWifi())
                .pool(dto.getPool())
                .airConditioner(dto.getAirConditioner())
                .parking(dto.getParking())
                .build();
    }
}