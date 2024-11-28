package by.ita.je.services;

import by.ita.je.models.Booking;
import by.ita.je.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

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
}