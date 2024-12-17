package by.ita.je.services.utils;

import by.ita.je.models.*;

import java.time.LocalDate;
import java.util.UUID;

public abstract class TestUtils {

    protected Hotel buildHotel(UUID id, String name, String location, String description, String periodOfWork) {
        return Hotel.builder()
                .id(id)
                .name(name)
                .location(location)
                .description(description)
                .periodOfWork(periodOfWork)
                .build();
    }

    protected Amenities buildAmenities(UUID id, Boolean wifi, Boolean pool, Boolean airConditioner, Boolean parking){
        return Amenities.builder()
                .id(id)
                .wifi(wifi)
                .pool(pool)
                .airConditioner(airConditioner)
                .parking(parking)
                .build();
    }

    protected Booking buildBooking(UUID id, LocalDate checkInDate,LocalDate checkOutDate){
        return Booking.builder()
                .id(id)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .build();
    }

    protected Notification buildNotification(UUID id, String message){
        return Notification.builder()
                .id(id)
                .message(message)
                .build();
    }

    protected Profile buildProfile(UUID id, String role, String name, String login, String password, Double balance){
        return Profile.builder()
                .id(id)
                .role(role)
                .name(name)
                .login(login)
                .password(password)
                .balance(balance)
                .build();
    }

    protected Room buildRoom(UUID id, Integer roomNumber, String type, Integer pricePerNight, Boolean isAvailable){
        return Room.builder()
                .id(id)
                .roomNumber(roomNumber)
                .type(type)
                .pricePerNight(pricePerNight)
                .isAvailable(isAvailable)
                .build();
    }

    protected Social buildSocial(UUID id, Double rating, Integer followersCount){
        return Social.builder()
                .id(id)
                .rating(rating)
                .followersCount(followersCount)
                .build();
    }
}
