package by.ita.je.services;

import by.ita.je.models.Hotel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
class HotelServiceIT {

    @Autowired
    private HotelService hotelService;

    @Test
    void shouldSortHotelsByName() {
        List<Hotel> sortedByName = hotelService.getSortedHotels("name");

        assertThat(sortedByName)
                .isSortedAccordingTo((h1, h2) -> h1.getName().compareToIgnoreCase(h2.getName()));
    }

    @Test
    void shouldSortHotelsByLocation() {
        List<Hotel> sortedByLocation = hotelService.getSortedHotels("location");

        assertThat(sortedByLocation)
                .isSortedAccordingTo((h1, h2) -> h1.getLocation().compareToIgnoreCase(h2.getLocation()));
    }

    @Test
    void shouldSortHotelsByRating() {
        List<Hotel> sortedByRating = hotelService.getSortedHotels("rating");

        assertThat(sortedByRating)
                .isSortedAccordingTo((h1, h2) -> Double.compare(
                        h2.getSocial().getRating(),
                        h1.getSocial().getRating()
                ));
    }

    @Test
    void shouldSortHotelsByAmenities() {
        List<Hotel> sortedByAmenities = hotelService.getSortedHotels("amenities");

        assertThat(sortedByAmenities)
                .isSortedAccordingTo((h1, h2) -> {
                    int compareWifi = Boolean.compare(h2.getAmenities().getWifi(), h1.getAmenities().getWifi());
                    if (compareWifi != 0) return compareWifi;

                    int comparePool = Boolean.compare(h2.getAmenities().getPool(), h1.getAmenities().getPool());
                    if (comparePool != 0) return comparePool;

                    int compareAirConditioner = Boolean.compare(h2.getAmenities().getAirConditioner(), h1.getAmenities().getAirConditioner());
                    if (compareAirConditioner != 0) return compareAirConditioner;

                    return Boolean.compare(h2.getAmenities().getParking(), h1.getAmenities().getParking());
                });
    }


}