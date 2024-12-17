package by.ita.je.services;

import by.ita.je.models.Booking;
import by.ita.je.models.Hotel;
import by.ita.je.models.Profile;
import by.ita.je.models.Room;
import by.ita.je.repositories.BookingRepository;
import by.ita.je.repositories.HotelRepository;
import by.ita.je.repositories.ProfileRepository;
import by.ita.je.repositories.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final ProfileRepository profileRepository;
    private final RoomRepository roomRepository;

    public Booking create(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking read(UUID id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public List<Booking> readAll() {
        return bookingRepository.findAll();
    }

    public Booking update(Booking booking) {
        if (bookingRepository.existsById(booking.getId())) {
            return bookingRepository.save(booking);
        }
        return null;
    }

    public Booking delete(UUID id) {
        Booking foundBooking = bookingRepository.findById(id).orElse(null);
        if (foundBooking != null) {
            bookingRepository.delete(foundBooking);
        }
        return foundBooking;
    }

    public void deleteAll() {
        bookingRepository.deleteAll();
    }

    @Transactional
    public void updateBookingsForRoom(UUID roomId, Hotel hotel) {

        Room room = hotel.getRooms().stream()
                .filter(r -> r.getId().equals(roomId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Комната с ID " + roomId + " не найдена"));

        room.getBookings().forEach(booking -> booking.setRoom(room));

        bookingRepository.saveAll(room.getBookings());
    }

    @Transactional
    public String bookRoom(String login, String hotelName, Integer roomNumber, LocalDate checkInDate, LocalDate checkOutDate) {

        Optional<Hotel> hotelOptional = hotelRepository.findByName(hotelName);
        if (hotelOptional.isEmpty()) {
            throw new IllegalArgumentException("Hotel not found");
        }

        Hotel hotel = hotelOptional.get();

        Optional<Room> roomOptional = hotel.getRooms().stream()
                .filter(room -> room.getRoomNumber().equals(roomNumber))
                .findFirst();

        if (roomOptional.isEmpty()) {
            throw new IllegalArgumentException("Room not found in the specified hotel");
        }

        Room room = roomOptional.get();

        if (!room.getIsAvailable()) {
            throw new IllegalStateException("Room is not available");
        }

        Optional<Profile> profileOptional = profileRepository.findByLogin(login);
        if (profileOptional.isEmpty()) {
            throw new IllegalArgumentException("Profile not found");
        }

        Profile profile = profileOptional.get();

        boolean hasConflict = bookingRepository.findByIdAndDateRange(room.getId(), checkInDate, checkOutDate)
                .stream()
                .anyMatch(booking -> !booking.getCheckOutDate().isBefore(checkInDate) &&
                        !booking.getCheckInDate().isAfter(checkOutDate));

        if (hasConflict) {
            throw new IllegalStateException("Room is already booked for the selected dates");
        }

        long numberOfNights = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        int totalCost = (int) numberOfNights * room.getPricePerNight();

        if (profile.getBalance() < totalCost) {
            throw new IllegalStateException("Insufficient balance");
        }

        profile.setBalance(profile.getBalance() - totalCost);
        profileRepository.save(profile);

        Booking booking = Booking.builder()
                .room(room)
                .profile(profile)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .build();

        bookingRepository.save(booking);

        room.setIsAvailable(false);
        roomRepository.save(room);

        return "Room successfully booked";
    }
}