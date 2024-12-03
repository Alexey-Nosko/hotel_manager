package by.ita.je.services;

import by.ita.je.dto.HotelDto;
import by.ita.je.mappers.HotelMapper;
import by.ita.je.models.Hotel;
import by.ita.je.models.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final RestTemplate restTemplate;
    private final HotelMapper hotelMapper;

    public List<Hotel> getHotelListByFilter(String sortBy) {

        ResponseEntity<List<HotelDto>> response = restTemplate.exchange(
                "/read/all/sorted?sortBy=" + sortBy,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<HotelDto>>() {}
        );

        List<HotelDto> hotelDtos = response.getBody();
        if (hotelDtos == null) {
            return Collections.emptyList();
        }

        return hotelDtos.stream()
                .map(hotelMapper::toEntity)
                .collect(Collectors.toList());
    }

    public Hotel createHotel(Hotel hotel){

        restTemplate.postForObject("/create",hotelMapper.toDto(hotel), HotelDto.class);

        return hotel;
    }

    public Hotel readHotel(UUID uuid){

        return hotelMapper.toEntity(restTemplate.getForObject("/read/" + uuid, HotelDto.class));

    }

    public Hotel updateHotel(UUID uuid, Hotel transferredHotel){

        HotelDto hotelDto = restTemplate.getForObject("/read/" + uuid, HotelDto.class);
        Hotel hotel = hotelMapper.toEntity(hotelDto);

        hotel.setName(transferredHotel.getName());
        hotel.setLocation(transferredHotel.getLocation());
        hotel.setDescription(transferredHotel.getDescription());
        hotel.setPeriodOfWork(transferredHotel.getPeriodOfWork());

        restTemplate.put("/update/"+uuid,hotelMapper.toDto(hotel), HotelDto.class);

        return hotel;
    }

    public void deleteHotel(UUID uuid){

        restTemplate.delete("/delete/"+uuid);
    }

    public void hotelManagerRegistration(UUID uuid, Profile profile){

        HotelDto hotelDto = restTemplate.getForObject("/read/" + uuid, HotelDto.class);
        Hotel hotel = hotelMapper.toEntity(hotelDto);

        List<Profile> profiles =  hotel.getProfiles();
        profiles.add(profile);
        hotel.setProfiles(profiles);
        restTemplate.postForObject("/update/"+uuid,hotelMapper.toDto(hotel), HotelDto.class);
    }

}
