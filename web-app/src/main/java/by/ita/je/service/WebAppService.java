package by.ita.je.service;

import by.ita.je.dto.HotelDto;
import by.ita.je.dto.ProfileDto;
import by.ita.je.dto.RoomDto;
import by.ita.je.mappers.HotelMapper;
import by.ita.je.mappers.ProfileMapper;
import by.ita.je.mappers.RoomMapper;
import by.ita.je.models.Hotel;
import by.ita.je.models.Profile;
import by.ita.je.models.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebAppService {

    private final RestTemplate restTemplate;
    private final HotelMapper hotelMapper;
    private final ProfileMapper profileMapper;
    private final RoomMapper roomMapper;
    private final PasswordEncoder passwordEncoder;

    public void clientRegistration(Profile profile){

        profile.setPassword(passwordEncoder.encode(profile.getPassword()));

        restTemplate.postForObject("/profile/create", profileMapper.toDto(profile), ProfileDto.class);
    }

    public void passwordReset(String password){

        String encryptedPassword = passwordEncoder.encode(password);

        restTemplate.postForObject("/password/reset?login="+getCurrentUsername()+"&password="+encryptedPassword, null, ProfileDto.class);
    }

    public List<Hotel> filterHotels(
            Optional<String> name,
            Optional<String> location,
            Optional<Double> minRating,
            Optional<Boolean> wifi,
            Optional<Boolean> pool,
            Optional<Boolean> airConditioner,
            Optional<Boolean> parking
    ) {

        StringBuilder urlBuilder = new StringBuilder("/hotels/filter?");

        name.ifPresent(n -> urlBuilder.append("name=").append(n).append("&"));
        location.ifPresent(l -> urlBuilder.append("location=").append(l).append("&"));
        minRating.ifPresent(r -> urlBuilder.append("minRating=").append(r).append("&"));
        wifi.ifPresent(w -> urlBuilder.append("wifi=").append(w).append("&"));
        pool.ifPresent(p -> urlBuilder.append("pool=").append(p).append("&"));
        airConditioner.ifPresent(a -> urlBuilder.append("airConditioner=").append(a).append("&"));
        parking.ifPresent(p -> urlBuilder.append("parking=").append(p).append("&"));

        if (urlBuilder.charAt(urlBuilder.length() - 1) == '&') {
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }

        String url = urlBuilder.toString();

        HotelDto[] response = restTemplate.getForObject(url, HotelDto[].class);

        return List.of(response).stream().map(hotelMapper::toEntity).toList();
    }

    public Hotel getSocialByHotelName(String name) {

        HotelDto hotelDto = restTemplate.getForObject("/hotel/find/by/name?name=" + name, HotelDto.class);

        return hotelMapper.toEntity(hotelDto);
    }

    public Hotel create(Hotel hotel) {

        restTemplate.postForObject("/hotel/create", hotelMapper.toDto(hotel), HotelDto.class);
        return hotel;
    }

    public Hotel update(Hotel hotel, String name) {

        restTemplate.postForObject("/hotel/update?name=" + name, hotelMapper.toDto(hotel), HotelDto.class);
        return hotel;
    }

    public Hotel read(String name) {

        HotelDto hotelDto = restTemplate.getForObject("/hotel/find/by/name?name=" + name, HotelDto.class);
        return hotelMapper.toEntity(hotelDto);
    }

    public void delete(String name) {

        restTemplate.delete("/hotel/delete?name=" + name);
    }

    public void hotelManagerRegistration(String hotelName, Profile profile) {

        profile.setPassword(passwordEncoder.encode(profile.getPassword()));

        restTemplate.postForObject("/manager/registration?name=" + hotelName, profileMapper.toDto(profile), ProfileDto.class);
    }

    public void changeHotelRoomConfiguration(String hotelName, Room room) {

        restTemplate.postForObject("/change/configuration?name=" + hotelName, roomMapper.toDto(room), RoomDto.class);
    }

    public List<Room> viewAvailableRooms(String name) {

        ResponseEntity<List<RoomDto>> response = restTemplate.exchange(
                "/hotel/view/available/rooms?name=" + name,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RoomDto>>() {
                }
        );

        List<RoomDto> roomDtos = response.getBody();

        return roomDtos.stream().map(roomMapper::toEntity).toList();
    }

    public List<Room> viewBookedRooms(String name) {

        ResponseEntity<List<RoomDto>> response = restTemplate.exchange(
                "/hotel/view/booked/rooms?name=" + name,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RoomDto>>() {
                }
        );

        List<RoomDto> roomDtos = response.getBody();

        return roomDtos.stream().map(roomMapper::toEntity).toList();
    }

    public void bookingCancellation(String hotelName, String clientName) {

        restTemplate.postForObject("/booking/cancellation?name=" + hotelName + "&clientName=" + clientName, null, HotelDto.class);
    }

    public void rateHotel(String name, Double hotelEvaluation) {

        restTemplate.postForObject("/rate/hotel?name=" + name + "&hotelEvaluation=" + hotelEvaluation, null, HotelDto.class);
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();
        }
        return null;
    }

    public void bookmarkHotel(String name) {

        restTemplate.postForObject("/add/bookmark?name=" + name + "&login=" + getCurrentUsername(), null, HotelDto.class);
    }

    public List<Hotel> seeAllHotelsInBookmarks() {

        String url = "/see/all/hotels/in/bookmarks?login=" + getCurrentUsername();

        ResponseEntity<HotelDto[]> response = restTemplate.getForEntity(url, HotelDto[].class);

        HotelDto[] hotelDtos = response.getBody();
        if (hotelDtos == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(hotelDtos)
                .map(hotelMapper::toEntity)
                .collect(Collectors.toList());
    }

    public Profile getProfile() {

        String url = "/find?login=" + getCurrentUsername();
        ResponseEntity<ProfileDto> response = restTemplate.getForEntity(url, ProfileDto.class);
        return profileMapper.toEntity(response.getBody());
    }

    public void topUpBalance(Double balance){

        restTemplate.postForObject("/top/up/balance?login="+getCurrentUsername()+"&balance="+balance, null, ProfileDto.class);
    }

    public void bookRoom(String hotelName, Integer roomNumber, LocalDate checkInDate, LocalDate checkOutDate) {
        String url = String.format(
                "/book/room?login=%s&hotelName=%s&roomNumber=%d&checkInDate=%s&checkOutDate=%s",
                getCurrentUsername(), hotelName, roomNumber, checkInDate.toString(), checkOutDate.toString()
        );
        restTemplate.postForObject(url, null, ProfileDto.class);
    }


}
