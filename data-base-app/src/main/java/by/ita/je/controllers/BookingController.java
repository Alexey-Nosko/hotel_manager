package by.ita.je.controllers;

import by.ita.je.dto.BookingDto;
import by.ita.je.dto.HotelDto;
import by.ita.je.mappers.BookingMapper;
import by.ita.je.mappers.HotelMapper;
import by.ita.je.models.Booking;
import by.ita.je.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(path = "/database/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;
    private final HotelMapper hotelMapper;

    @PostMapping("/create")
    public void create(@RequestBody BookingDto bookingDto){
        Booking booking = bookingMapper.toEntity(bookingDto);
        bookingService.create(booking);
    }

    @GetMapping("/read/{id}")
    public BookingDto read(@PathVariable UUID id) {
        return bookingMapper.toDto(bookingService.read(id));
    }

    @GetMapping("/read/all")
    public List<BookingDto> readALL()
    {
        List<Booking> bookings = bookingService.readAll();
        List<BookingDto> bookingDtos = bookings.stream().map(bookingMapper::toDto).toList();
        return bookingDtos;
    }

    @PutMapping("/update/{uuid}")
    public BookingDto update(@RequestBody BookingDto bookingDto){

        Booking booking = bookingMapper.toEntity(bookingDto);

        return bookingMapper.toDto(bookingService.update(booking));
    }

    @DeleteMapping("/delete/{uuid}")
    public void delete(@PathVariable UUID uuid) {
        bookingService.delete(uuid);
    }

    @DeleteMapping("/delete/all")
    public void deleteAll() {
        bookingService.deleteAll();
    }

    @PutMapping("update/booking/for/room/{id}")
    public void updateBookingForRoom(@PathVariable UUID id,
                                     @RequestBody HotelDto hotelDto){

        bookingService.updateBookingsForRoom(id,hotelMapper.toEntity(hotelDto));
    }

    @PostMapping("book/room")
    public void bookRoom(@RequestParam String login,@RequestParam String hotelName,
                         @RequestParam Integer roomNumber, @RequestParam LocalDate checkInDate,
                         @RequestParam LocalDate checkOutDate){

        bookingService.bookRoom(login, hotelName, roomNumber, checkInDate, checkOutDate);
    }
}