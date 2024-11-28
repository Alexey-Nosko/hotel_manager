package by.ita.je.dto;

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
public class HotelDto {

    private UUID id;

    private String name;

    private Double rating;

    private String location;

    private String description;

    private String periodOfWork;

    private List<RoomDto> roomsDto;

    private AmenitiesDto amenitiesDto;

    private ManagerDto managerDto;

    private SocialDto socialDto;
}
