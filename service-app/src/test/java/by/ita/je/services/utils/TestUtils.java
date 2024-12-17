package by.ita.je.services.utils;

import by.ita.je.dto.HotelDto;
import by.ita.je.dto.ProfileDto;
import by.ita.je.models.Hotel;
import by.ita.je.models.Profile;

import java.util.UUID;

public abstract class TestUtils {

    protected static UUID uuid = UUID.randomUUID();

    protected Hotel buildHotel(UUID id, String name, String location, String description, String periodOfWork) {
        return Hotel.builder()
                .id(id)
                .name(name)
                .location(location)
                .description(description)
                .periodOfWork(periodOfWork)
                .build();
    }

    protected HotelDto buildHotelDto(UUID id, String name, String location, String description, String periodOfWork) {
        return HotelDto.builder()
                .id(id)
                .name(name)
                .location(location)
                .description(description)
                .periodOfWork(periodOfWork)
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

    protected ProfileDto buildProfileDto(UUID id, String role, String name, String login, String password, Double balance){
        return ProfileDto.builder()
                .id(id)
                .role(role)
                .name(name)
                .login(login)
                .password(password)
                .balance(balance)
                .build();
    }

}
