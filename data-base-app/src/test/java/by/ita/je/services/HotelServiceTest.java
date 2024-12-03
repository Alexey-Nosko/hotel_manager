package by.ita.je.services;

import by.ita.je.models.Hotel;
import by.ita.je.repositories.HotelRepository;
import by.ita.je.services.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest extends TestUtils {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    @Test
    void testGetSortedHotelsByName() {

        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");
        Hotel hotel2 = buildHotel(id,"Marriott ","Minsk, RB","Hotel with exquisite rooms and excellent service","All year round");

        List<Hotel> hotels = new ArrayList<>();
        hotels.add(hotel);
        hotels.add(hotel2);

        when(hotelRepository.findAll(Sort.by(Sort.Order.asc("name")))).thenReturn(hotels);

        List<Hotel> sortedHotels = hotelService.getSortedHotels("name");

        Mockito.verify(hotelRepository, Mockito.times(1)).findAll(Sort.by(Sort.Order.asc("name")));
        assertEquals(hotels, sortedHotels);
    }

    @Test
    void testGetSortedHotelsByLocation() {

        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");
        Hotel hotel2 = buildHotel(id,"Marriott ","Minsk, RB","Hotel with exquisite rooms and excellent service","All year round");

        List<Hotel> hotels = new ArrayList<>();
        hotels.add(hotel);
        hotels.add(hotel2);

        when(hotelRepository.findAll(Sort.by(Sort.Order.asc("location")))).thenReturn(hotels);

        List<Hotel> sortedHotels = hotelService.getSortedHotels("location");

        Mockito.verify(hotelRepository, Mockito.times(1)).findAll(Sort.by(Sort.Order.asc("location")));
        assertEquals(hotels, sortedHotels);
    }

    @Test
    void testGetSortedHotelsByRating() {

        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");
        Hotel hotel2 = buildHotel(id,"Marriott ","Minsk, RB","Hotel with exquisite rooms and excellent service","All year round");

        List<Hotel> hotels = new ArrayList<>();
        hotels.add(hotel);
        hotels.add(hotel2);

        when(hotelRepository.findAll(Sort.by(Sort.Order.desc("social.rating")))).thenReturn(hotels);

        List<Hotel> sortedHotels = hotelService.getSortedHotels("rating");

        Mockito.verify(hotelRepository, Mockito.times(1)).findAll(Sort.by(Sort.Order.desc("social.rating")));
        assertEquals(hotels, sortedHotels);
    }

    @Test
    void testGetSortedHotelsByAmenities() {

        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");
        Hotel hotel2 = buildHotel(id,"Marriott","Minsk, RB","Hotel with exquisite rooms and excellent service","All year round");

        List<Hotel> hotels = new ArrayList<>();
        hotels.add(hotel);
        hotels.add(hotel2);

        Sort amenitiesSort = Sort.by(
                Sort.Order.desc("amenities.wifi"),
                Sort.Order.desc("amenities.pool"),
                Sort.Order.desc("amenities.airConditioner"),
                Sort.Order.desc("amenities.parking")
        );
        when(hotelRepository.findAll(amenitiesSort)).thenReturn(hotels);

        List<Hotel> sortedHotels = hotelService.getSortedHotels("amenities");

        Mockito.verify(hotelRepository, Mockito.times(1)).findAll(amenitiesSort);
        assertEquals(hotels, sortedHotels);
    }
}