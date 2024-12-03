package by.ita.je.services;

import by.ita.je.models.*;
import by.ita.je.services.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BusinessServiceIT extends TestUtils {

    @Autowired
    private BusinessService businessService;

    @Test
    void testGetHotelListByFilter() {

        String sortBy = "name";
        List<Hotel> hotels = businessService.getHotelListByFilter(sortBy);

        assertNotNull(hotels);
    }

    @Test
    void testCreateHotel() {

        Booking booking = Booking.builder()
                .build();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Room room = Room.builder()
                .roomNumber(1)
                .type("single")
                .pricePerNight(100)
                .isAvailable(true)
                .build();
        room.setBookings(bookings);
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);
        Social social = Social.builder()
                .rating(4.5)
                .followersCount(7L)
                .build();
        Amenities amenities = Amenities.builder()
                .airConditioner(true)
                .pool(true)
                .wifi(true)
                .parking(true)
                .build();
        Notification notification = Notification.builder()
                .message("you are accepted")
                .build();
        List<Notification> notifications = new ArrayList<>();
        notifications.add(notification);
        Profile profile = Profile.builder()
                .name("Alexey")
                .role("Client")
                .login("login")
                .password("password")
                .build();
        profile.setNotifications(notifications);
        List<Profile> profiles = new ArrayList<>();
        profiles.add(profile);
        Hotel hotel = Hotel.builder()
                .name("Test Hotel")
                .location("Test Location")
                .description("Test Description")
                .periodOfWork("All time")
                .build();
        hotel.setRooms(rooms);
        hotel.setSocial(social);
        hotel.setAmenities(amenities);
        hotel.setProfiles(profiles);

        Hotel result = businessService.createHotel(hotel);

        assertEquals(hotel, result);
    }

    @Test
    void testReadHotel() {

        UUID uuid = UUID.fromString("f6d00b2c-8ab6-49e6-910d-c55fa7d1f9a6");

        Hotel result = businessService.readHotel(uuid);

        assertNotNull(result);
    }

    @Test
    void testUpdateHotel() {
        UUID uuid = UUID.fromString("f6d00b2c-8ab6-49e6-910d-c55fa7d1f9a6");

        Booking booking = Booking.builder()
                .build();
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        Room room = Room.builder()
                .roomNumber(1)
                .type("single")
                .pricePerNight(100)
                .isAvailable(true)
                .build();
        room.setBookings(bookings);
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);
        Social social = Social.builder()
                .rating(4.5)
                .followersCount(7L)
                .build();
        Amenities amenities = Amenities.builder()
                .airConditioner(true)
                .pool(true)
                .wifi(true)
                .parking(true)
                .build();
        Notification notification = Notification.builder()
                .message("you are accepted")
                .build();
        List<Notification> notifications = new ArrayList<>();
        notifications.add(notification);
        Profile profile = Profile.builder()
                .name("Alexey")
                .role("Client")
                .login("login")
                .password("password")
                .build();
        profile.setNotifications(notifications);
        List<Profile> profiles = new ArrayList<>();
        profiles.add(profile);
        Hotel hotel = Hotel.builder()
                .name("Test Hotel")
                .location("Test Location")
                .description("Test Description")
                .periodOfWork("All time")
                .build();
        hotel.setRooms(rooms);
        hotel.setSocial(social);
        hotel.setAmenities(amenities);
        hotel.setProfiles(profiles);


        Hotel result = businessService.updateHotel(uuid,hotel);

        assertNotNull(result);

    }

    @Test
    void testDeleteHotel() {
        UUID uuid = UUID.fromString("e8a3c5b7-7c1e-4f9a-ae8b-9c7f4e8d6a7b");

        businessService.deleteHotel(uuid);
    }
}