package by.ita.je.services;

import by.ita.je.models.Booking;
import by.ita.je.models.Hotel;
import by.ita.je.models.Room;
import by.ita.je.repositories.BookingRepository;
import by.ita.je.repositories.HotelRepository;
import by.ita.je.repositories.ProfileRepository;
import by.ita.je.repositories.RoomRepository;
import by.ita.je.services.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class BookingServiceIT extends TestUtils {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void testUpdateBookingsForRoom() {

        Hotel hotel = buildHotel(UUID.fromString("f6d00b2c-8ab6-49e6-910d-c55fa7d1f9a6"),"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");
        Room room = new Room();
        room.setId(UUID.fromString("7f9d2d0e-6a2e-4ed9-b9a1-14ec7e1d0f40"));
        room.setRoomNumber(101);
        hotel.setRooms(List.of(room));

        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        booking1.setRoom(room);
        booking2.setRoom(room);

        room.setBookings(List.of(booking1, booking2));

        bookingService.updateBookingsForRoom(UUID.fromString("7f9d2d0e-6a2e-4ed9-b9a1-14ec7e1d0f40"), hotel);
    }

    @Test
    void testBookRoom_Integration_HappyPath() {

        LocalDate checkInDate = LocalDate.of(2024, 12, 20);
        LocalDate checkOutDate = LocalDate.of(2024, 12, 22);

        bookingService.bookRoom("al", "Sunset Resort", 102, checkInDate, checkOutDate);
    }
}