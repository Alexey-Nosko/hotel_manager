package by.ita.je.services;

import by.ita.je.models.Booking;
import by.ita.je.models.Hotel;
import by.ita.je.models.Profile;
import by.ita.je.models.Room;
import by.ita.je.repositories.BookingRepository;
import by.ita.je.repositories.HotelRepository;
import by.ita.je.repositories.ProfileRepository;
import by.ita.je.repositories.RoomRepository;
import by.ita.je.services.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest extends TestUtils {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void testCreate() {
        UUID id = UUID.randomUUID();
        Booking booking = buildBooking(id, LocalDate.of(2024, 12, 3),LocalDate.of(2024, 12, 12));

        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking savedBooking = bookingService.create(booking);

        assertNotNull(savedBooking);
        assertEquals(savedBooking, booking);
        Mockito.verify(bookingRepository, Mockito.times(1)).save(booking);
    }

    @Test
    void testRead() {
        UUID id = UUID.randomUUID();
        Booking booking = buildBooking(id, LocalDate.of(2024, 12, 3),LocalDate.of(2024, 12, 12));

        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));

        Booking foundBooking = bookingService.read(id);

        assertNotNull(foundBooking);
        assertEquals(foundBooking, booking);
        Mockito.verify(bookingRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void testReadAll() {
        UUID id = UUID.randomUUID();
        Booking booking = buildBooking(id, LocalDate.of(2024, 12, 3),LocalDate.of(2024, 12, 12));

        when(bookingRepository.findAll()).thenReturn(List.of(booking, booking));

        List<Booking> bookings = bookingService.readAll();

        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        Assertions.assertTrue(bookings.contains(booking));
        Mockito.verify(bookingRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testUpdate() {
        UUID id = UUID.randomUUID();
        Booking booking = buildBooking(id, LocalDate.of(2024, 12, 3),LocalDate.of(2024, 12, 12));

        when(bookingRepository.existsById(id)).thenReturn(true);
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking updatedBooking = bookingService.update(booking);

        assertNotNull(updatedBooking);
        assertEquals(updatedBooking, booking);
        Mockito.verify(bookingRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(bookingRepository, Mockito.times(1)).save(booking);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        Booking booking = buildBooking(id, LocalDate.of(2024, 12, 3),LocalDate.of(2024, 12, 12));

        when(bookingRepository.findById(id)).thenReturn(Optional.of(booking));

        Booking deletedBooking = bookingService.delete(id);

        assertNotNull(deletedBooking);
        assertEquals(id, deletedBooking.getId());
        Mockito.verify(bookingRepository, Mockito.times(1)).findById(id);
        Mockito.verify(bookingRepository, Mockito.times(1)).delete(booking);
    }

    @Test
    void deleteAll() {
        bookingService.deleteAll();

        Mockito.verify(bookingRepository, Mockito.times(1)).deleteAll();
    }

    @Test
    void testUpdateBookingsForRoom() {

        UUID roomId = UUID.randomUUID();
        Hotel hotel = new Hotel();
        Room room = new Room();
        room.setId(roomId);
        Booking booking1 = new Booking();
        Booking booking2 = new Booking();
        room.setBookings(List.of(booking1, booking2));
        hotel.setRooms(List.of(room));
        bookingService.updateBookingsForRoom(roomId, hotel);

        Mockito.verify(bookingRepository, Mockito.times(1)).saveAll(room.getBookings());
        assertEquals((booking1.getRoom()),room);
        assertEquals((booking2.getRoom()),room);
    }

    @Test
    void testBookRoom_HappyPath() {
        String login = "user123";
        String hotelName = "Test Hotel";
        Integer roomNumber = 101;
        LocalDate checkInDate = LocalDate.of(2024, 12, 20);
        LocalDate checkOutDate = LocalDate.of(2024, 12, 25);
        Integer pricePerNight = 100;
        Double balance = 1000.0;

        Hotel hotel = new Hotel();
        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setIsAvailable(true);
        room.setPricePerNight(pricePerNight);

        Profile profile = new Profile();
        profile.setLogin(login);
        profile.setBalance(balance);

        hotel.setRooms(List.of(room));

        when(hotelRepository.findByName(hotelName)).thenReturn(Optional.of(hotel));
        when(profileRepository.findByLogin(login)).thenReturn(Optional.of(profile));
        when(bookingRepository.findByIdAndDateRange(room.getId(), checkInDate, checkOutDate))
                .thenReturn(List.of());

        String result = bookingService.bookRoom(login, hotelName, roomNumber, checkInDate, checkOutDate);

        Mockito.verify(profileRepository,  Mockito.times(1)).save(profile);
        Mockito.verify(bookingRepository,  Mockito.times(1)).save( Mockito.any(Booking.class));
        Mockito.verify(roomRepository,  Mockito.times(1)).save(room);

        assertEquals((profile.getBalance()),balance - (pricePerNight * 5));
        assertEquals((result),("Room successfully booked"));
    }
}