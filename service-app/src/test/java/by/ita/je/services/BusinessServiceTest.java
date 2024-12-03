package by.ita.je.services;

import by.ita.je.dto.HotelDto;
import by.ita.je.mappers.HotelMapper;
import by.ita.je.models.Hotel;
import by.ita.je.models.Profile;
import by.ita.je.services.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.core.ParameterizedTypeReference;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class BusinessServiceTest extends TestUtils {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private BusinessService businessService;


    @Test
    void testGetHotelListByFilter() {
        String sortBy = "name";

        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");
        HotelDto hotelDto = buildHotelDto(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");

        List<HotelDto> hotelDtos = Collections.singletonList(hotelDto);

        ResponseEntity<List<HotelDto>> responseEntity = ResponseEntity.ok(hotelDtos);
        when(restTemplate.exchange(
                Mockito.eq("/read/all/sorted?sortBy=" + sortBy),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(),
                Mockito.eq(new ParameterizedTypeReference<List<HotelDto>>() {})))
                .thenReturn(responseEntity);

        when(hotelMapper.toEntity(hotelDto)).thenReturn(hotel);

        List<Hotel> result = businessService.getHotelListByFilter(sortBy);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(hotel, result.get(0));

        verify(restTemplate,  Mockito.times(1)).exchange(
                Mockito.eq("/read/all/sorted?sortBy=" + sortBy),
                Mockito.eq(HttpMethod.GET),
                Mockito.any(),
                Mockito.eq(new ParameterizedTypeReference<List<HotelDto>>() {})
        );

        verify(hotelMapper,  Mockito.times(1)).toEntity(hotelDto);
    }

    @Test
    void testCreateHotel() {
        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");
        HotelDto hotelDto = buildHotelDto(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");

        when(hotelMapper.toDto(hotel)).thenReturn(hotelDto);

        businessService.createHotel(hotel);

        verify(restTemplate,  Mockito.times(1)).postForObject("/create", hotelDto, HotelDto.class);
    }

    @Test
    void testReadHotel() {
        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");
        HotelDto hotelDto = buildHotelDto(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");


        when(restTemplate.getForObject("/read/" + uuid, HotelDto.class)).thenReturn(hotelDto);
        when(hotelMapper.toEntity(hotelDto)).thenReturn(hotel);

        Hotel result = businessService.readHotel(uuid);

        assertNotNull(result);
        verify(restTemplate,  Mockito.times(1)).getForObject("/read/" + uuid, HotelDto.class);
        verify(hotelMapper,  Mockito.times(1)).toEntity(hotelDto);
    }

    @Test
    void testUpdateHotel() {

        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");
        HotelDto hotelDto = buildHotelDto(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");


        when(restTemplate.getForObject("/read/" + uuid, HotelDto.class)).thenReturn(hotelDto);
        when(hotelMapper.toEntity(hotelDto)).thenReturn(hotel);
        when(hotelMapper.toDto(hotel)).thenReturn(hotelDto);

        businessService.updateHotel(uuid, hotel);

        verify(restTemplate,  Mockito.times(1)).getForObject("/read/" + uuid, HotelDto.class);
        verify(restTemplate,  Mockito.times(1)).postForObject("/update/" + uuid, hotelDto, HotelDto.class);
    }

    @Test
    void testDeleteHotel() {
        UUID uuid = UUID.randomUUID();

        businessService.deleteHotel(uuid);

        verify(restTemplate,  Mockito.times(1)).delete("/delete/" + uuid);
    }

    /*@Test
    void testHotelManagerRegistration() {
        UUID uuid = UUID.randomUUID();
        HotelDto hotelDto = new HotelDto();
        Hotel hotel = new Hotel();
        Profile profile = new Profile();
        List<Profile> profiles = List.of();

        when(restTemplate.getForObject("/read/" + uuid, HotelDto.class)).thenReturn(hotelDto);
        when(hotelMapper.toEntity(hotelDto)).thenReturn(hotel);
        when(hotelMapper.toDto(hotel)).thenReturn(hotelDto);

        hotel.setProfiles(profiles);

        businessService.hotelManagerRegistration(uuid, profile);

        Mockito.verify(restTemplate,  Mockito.times(1)).getForObject("/read/" + uuid, HotelDto.class);
        Mockito.verify(restTemplate,  Mockito.times(1)).postForObject("/update/" + uuid, hotelDto, HotelDto.class);
    }*/

}