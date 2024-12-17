package by.ita.je.services;

import by.ita.je.models.*;
import by.ita.je.repositories.HotelRepository;
import by.ita.je.services.utils.TestUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest extends TestUtils {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    @Test
    void testCreate() {
        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");

        when(hotelRepository.save(hotel)).thenReturn(hotel);

        Hotel savedHotel = hotelService.create(hotel);

        assertNotNull(savedHotel);
        assertEquals(savedHotel,hotel);
        Mockito.verify(hotelRepository,  Mockito.times(1)).save(hotel);
    }

    @Test
    void testRead() {
        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");

        when(hotelRepository.findById(id)).thenReturn(Optional.of(hotel));

        Hotel foundHotel = hotelService.read(id);

        assertNotNull(foundHotel);
        assertEquals(foundHotel,hotel);
        Mockito.verify(hotelRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void testReadAll() {
        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");

        when(hotelRepository.findAll()).thenReturn(List.of(hotel,hotel));

        List<Hotel> hotels = hotelService.readAll();

        assertNotNull(hotels);
        assertEquals(2, hotels.size());
        Assertions.assertTrue(hotels.contains(hotel));
        Mockito.verify(hotelRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testUpdate() {
        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");

        when(hotelRepository.existsById(id)).thenReturn(true);
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        Hotel updatedHotel = hotelService.update(hotel);

        assertNotNull(updatedHotel);
        assertEquals(updatedHotel,hotel);
        Mockito.verify(hotelRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(hotelRepository, Mockito.times(1)).save(hotel);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");

        when(hotelRepository.findById(id)).thenReturn(Optional.of(hotel));

        Hotel deletedHotel = hotelService.delete(id);

        assertNotNull(deletedHotel);
        assertEquals(id, deletedHotel.getId());
        Mockito.verify(hotelRepository,  Mockito.times(1)).findById(id);
        Mockito.verify(hotelRepository,  Mockito.times(1)).delete(hotel);
    }
    @Test
    void deleteAll() {
        hotelService.deleteAll();

        Mockito.verify(hotelRepository,  Mockito.times(1)).deleteAll();
    }

    @Test
    void testFilterHotels() {

        Optional<String> name = Optional.of("Grand Hotel");
        Optional<String> location = Optional.of("New York");
        Optional<Double> minRating = Optional.of(4.0);
        Optional<Boolean> wifi = Optional.of(true);
        Optional<Boolean> pool = Optional.of(true);
        Optional<Boolean> airConditioner = Optional.of(true);
        Optional<Boolean> parking = Optional.of(true);

        Hotel hotel = new Hotel();
        hotel.setName("Grand Hotel");
        hotel.setLocation("New York");

        Social social = new Social();
        social.setRating(4.5);
        hotel.setSocial(social);

        Amenities amenities = new Amenities();
        amenities.setWifi(true);
        amenities.setPool(true);
        amenities.setAirConditioner(true);
        amenities.setParking(true);
        hotel.setAmenities(amenities);

        List<Hotel> hotels = List.of(hotel);

        Mockito.when(hotelRepository.findAll(Mockito.any(Specification.class))).thenReturn(hotels);

        List<Hotel> result = hotelService.filterHotels(name, location, minRating, wifi, pool, airConditioner, parking);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Grand Hotel", result.get(0).getName());
        assertEquals("New York", result.get(0).getLocation());
        assertEquals(4.5, result.get(0).getSocial().getRating());

        Mockito.verify(hotelRepository,  Mockito.times(1)).findAll( Mockito.any(Specification.class));
    }

    @Test
    void testFindHotelByName() {
        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");

        Mockito.when(hotelRepository.findByName("Sunset Resort")).thenReturn(Optional.of(hotel));

        Hotel result = hotelService.findHotelByName("Sunset Resort");

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Sunset Resort", result.getName());
        Mockito.verify(hotelRepository, Mockito.times(1)).findByName("Sunset Resort");
    }

    @Test
    @Transactional
    void testUpdateHotelByName() {
        // Arrange
        String name = "Grand Hotel";
        Hotel existingHotel = new Hotel();
        existingHotel.setName(name);
        existingHotel.setProfiles(new ArrayList<>());

        Hotel updatedHotelData = new Hotel();
        updatedHotelData.setLocation("Paris");
        updatedHotelData.setDescription("Updated description");
        updatedHotelData.setPeriodOfWork("All time");
        updatedHotelData.setRooms(List.of(new Room()));

        Amenities updatedAmenities = new Amenities();
        updatedHotelData.setAmenities(updatedAmenities);

        Social updatedSocial = new Social();
        updatedHotelData.setSocial(updatedSocial);

        Profile profile = new Profile();
        updatedHotelData.setProfiles(List.of(profile));

        when(hotelRepository.findByName(name)).thenReturn(Optional.of(existingHotel));

        boolean result = hotelService.updateHotelByName(name, updatedHotelData);

        assertTrue(result);
        assertEquals("Paris", existingHotel.getLocation());
        assertEquals("Updated description", existingHotel.getDescription());
        assertEquals("All time", existingHotel.getPeriodOfWork());
        assertNotNull(existingHotel.getAmenities());
        assertNotNull(existingHotel.getSocial());
        assertNotNull(existingHotel.getRooms());
        assertEquals(1, existingHotel.getProfiles().size()); // Проверяем, что профиль добавлен

        Mockito.verify(hotelRepository, Mockito.times(1)).findByName(name);
        Mockito.verify(hotelRepository, Mockito.times(1)).save(existingHotel);
    }

    @Test
    void testDeleteHotelByName() {

        String name = "Grand Hotel";

        hotelService.deleteHotelByName(name);

        Mockito.verify(hotelRepository, Mockito.times(1)).deleteByName(name);
    }
}
