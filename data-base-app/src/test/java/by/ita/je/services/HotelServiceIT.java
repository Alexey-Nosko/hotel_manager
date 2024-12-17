package by.ita.je.services;

import by.ita.je.models.Hotel;
import by.ita.je.services.utils.TestUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class HotelServiceIT extends TestUtils {

    @Autowired
    private HotelService hotelService;

    @Test
    public void testFindHotelByName() {

        Hotel hotel = buildHotel(UUID.fromString("f6d00b2c-8ab6-49e6-910d-c55fa7d1f9a6"),"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");

        Hotel foundHotel = hotelService.findHotelByName("Sunset Resort");

        assertEquals(hotel.getName(), foundHotel.getName());
    }

    @Test
    public void testFilterHotels(){

        List<Hotel> filteredHotels = hotelService.filterHotels(
                Optional.of("Sunset Resort"),
                Optional.of("Miami, FL"),
                Optional.of(4.0),
                Optional.of(false),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        assertEquals(1, filteredHotels.size());
    }

    @Test
    public void testUpdateHotelByName() {

        Hotel updatedHotelData = new Hotel();
        updatedHotelData.setLocation("New Location");
        updatedHotelData.setDescription("New Description");
        updatedHotelData.setPeriodOfWork("All time");

        boolean result = hotelService.updateHotelByName("Marriott", updatedHotelData);

        assertTrue(result);
    }

    @Test
    @Transactional
    public void testDeleteHotelByName_HotelExists() {

        hotelService.deleteHotelByName("Marriott");

    }
}