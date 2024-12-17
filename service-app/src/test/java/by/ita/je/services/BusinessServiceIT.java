package by.ita.je.services;

import by.ita.je.dto.HotelDto;
import by.ita.je.dto.ProfileDto;
import by.ita.je.models.*;
import by.ita.je.services.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BusinessServiceIT extends TestUtils {

    @Autowired
    private BusinessService businessService;

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
                .followersCount(7)
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
                .name("Grand Hotel")
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
    void testUpdateHotel() {

        String name = "Sunset Resort";

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
                .followersCount(7)
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

        Hotel result = businessService.updateHotel(name,hotel);

        assertNotNull(result);
    }

    @Test
    void testDeleteHotel() {

        String name = "Marriott";

        businessService.deleteHotel(name);
    }

    @Test
    void testFindByLoginIntegration() {

        String login = "al";
        ProfileDto profileDto1 = businessService.findByLogin(login);
        assertEquals(2050.0,profileDto1.getBalance());
        assertEquals("al",profileDto1.getLogin());
        assertNotNull(profileDto1);
    }

    @Test
    void testFilterHotels() {

        List<Hotel> hotels = businessService.filterHotels(Optional.of("Sunset Resort"), Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        assertNotNull(hotels);
        assertEquals(1, hotels.size());
        assertEquals("Sunset Resort", hotels.get(0).getName());
    }

    @Test
    void testFindHotelByName() {

        Hotel hotel = businessService.findHotelByName("Sunset Resort");

        assertNotNull(hotel);
        assertEquals("Sunset Resort", hotel.getName());
    }

    @Test
    void viewAvailableRooms() {

        String hotelName = "Sunset Resort";

        List<Room> availableRooms = businessService.viewAvailableRooms(hotelName);

        assertNotNull(availableRooms);
        assertFalse(availableRooms.isEmpty());
        assertTrue(availableRooms.stream().allMatch(Room::getIsAvailable));
    }

    @Test
    void viewBookedRooms() {
        String hotelName = "Sunset Resort";

        List<Room> bookedRooms = businessService.viewBookedRooms(hotelName);

       assertEquals(bookedRooms, Collections.emptyList());
    }

    @Test
    void testHotelManagerRegistration() {
        String name = "Sunset Resort";
        Profile profile = Profile.builder()
                .role("MANAGER")
                .name("max")
                .login("log")
                .password("pass")
                .build();


        businessService.hotelManagerRegistration(name, profile);
    }

    @Test
    void testChangeHotelRoomConfiguration() {
        String hotelName = "Grand Hotel";
        Room adventRoom = new Room();
        adventRoom.setRoomNumber(1);
        adventRoom.setType("Double");
        adventRoom.setPricePerNight(200);

        businessService.changeHotelRoomConfiguration(hotelName, adventRoom);
    }

    @Test
    void rateHotel() {

        String hotelName = "Sunset Resort";
        Double hotelEvaluation = 4.5;

        businessService.rateHotel(hotelName, hotelEvaluation);
    }

    /*@Test
    void bookmarkHotel() {

        String hotelName = "Sunset Resort";
        String login = "al";

        businessService.bookmarkHotel(hotelName, login);
    }*/

    @Test
    void seeAllHotelsInBookmarks() {

        String login = "al";

        List<Hotel> bookmarkedHotels = businessService.seeAllHotelsInBookmarks(login);

        assertFalse(bookmarkedHotels.isEmpty());
        assertEquals("Sunset Resort", bookmarkedHotels.get(0).getName());
    }

    @Test
    void topUpBalance() {
        String login = "al";
        Double balance = 50.0;

        businessService.topUpBalance(login, balance);
    }

    @Test
    void bookRoom() {
        String login = "al";
        String hotelName = "Grand Hotel";
        Integer roomNumber = 1;
        LocalDate checkInDate = LocalDate.of(2024, 12, 20);
        LocalDate checkOutDate = LocalDate.of(2024, 12, 25);

        businessService.bookRoom(login, hotelName, roomNumber, checkInDate, checkOutDate);
    }

}