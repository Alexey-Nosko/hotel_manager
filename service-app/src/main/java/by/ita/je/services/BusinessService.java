package by.ita.je.services;

import by.ita.je.dto.HotelDto;
import by.ita.je.dto.ProfileDto;
import by.ita.je.mappers.HotelMapper;
import by.ita.je.mappers.NotificationMapper;
import by.ita.je.mappers.ProfileMapper;
import by.ita.je.mappers.RoomMapper;
import by.ita.je.models.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BusinessService {

    private final RestTemplate restTemplate;
    private final HotelMapper hotelMapper;
    private final ProfileMapper profileMapper;
    private final RoomMapper roomMapper;
    private final NotificationMapper notificationMapper;

    public void clientRegistration(Profile profile){

        restTemplate.postForObject("/profile/create", profileMapper.toDto(profile), ProfileDto.class);

    }

    public void passwordReset(String login, String password){

        restTemplate.put("/profile/password/reset?login="+login+"&password="+password, null, ProfileDto.class);

    }

    public ProfileDto findByLogin(String login) {
        String url = "/profile/find?login=" + login;
        try {
            ResponseEntity<ProfileDto> response = restTemplate.getForEntity(url, ProfileDto.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new RuntimeException("Profile not found for login: " + login);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching profile for login: " + login, e);
        }
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
        StringBuilder urlBuilder = new StringBuilder("/hotel/hotels/filter?");

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

    public Hotel findHotelByName(String name) {

        HotelDto hotelDto = restTemplate.getForObject("/hotel/find/by/name?name=" + name, HotelDto.class);

        return hotelMapper.toEntity(hotelDto);
    }

    public Hotel createHotel(Hotel hotel) {

        restTemplate.postForObject("/hotel/create", hotelMapper.toDto(hotel), HotelDto.class);

        return hotel;
    }

    public Hotel updateHotel(String name, Hotel transferredHotel) {

        HotelDto hotelDto = restTemplate.getForObject("/hotel/find/by/name?name=" + name, HotelDto.class);
        Hotel hotel = hotelMapper.toEntity(hotelDto);

        hotel.setLocation(transferredHotel.getLocation());
        hotel.setDescription(transferredHotel.getDescription());
        hotel.setPeriodOfWork(transferredHotel.getPeriodOfWork());
        hotel.setRooms(transferredHotel.getRooms());
        hotel.setAmenities(transferredHotel.getAmenities());
        hotel.setSocial(transferredHotel.getSocial());
        hotel.setProfiles(transferredHotel.getProfiles());

        restTemplate.put("/hotel/update/by/name?name=" + name, hotelMapper.toDto(hotel), HotelDto.class);

        return hotel;
    }

    public void deleteHotel(String name) {

        restTemplate.delete("/hotel/delete/by/name?name=" + name);
    }

    public void hotelManagerRegistration(String name, Profile profile) {

        HotelDto hotelDto = restTemplate.getForObject("/hotel/find/by/name?name=" + name, HotelDto.class);
        Hotel hotel = hotelMapper.toEntity(hotelDto);

        List<Profile> profiles = hotel.getProfiles();
        profiles.add(profile);
        hotel.setProfiles(profiles);

        restTemplate.postForObject("/profile/manager/registration", hotelMapper.toDto(hotel), HotelDto.class);

        restTemplate.put("/hotel/update/by/name?name=" + name, hotelMapper.toDto(hotel), HotelDto.class);
    }

    public void changeHotelRoomConfiguration(String hotelName, Room adventRoom) {

        HotelDto hotelDto = restTemplate.getForObject("/hotel/find/by/name?name=" + hotelName, HotelDto.class);

        if (hotelDto == null) {
            throw new IllegalArgumentException("Hotel with name " + hotelName + " not found");
        }

        Hotel hotel = hotelMapper.toEntity(hotelDto);

        Room room = hotel.getRooms().stream()
                .filter(r -> r.getRoomNumber().equals(adventRoom.getRoomNumber()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Room with number " + adventRoom.getRoomNumber() + " not found in hotel " + hotelName));

        room.setType(adventRoom.getType());
        room.setPricePerNight(adventRoom.getPricePerNight());

        System.out.println(room.getId());

        restTemplate.put("/room/update/change/configuration/" + room.getId(), hotelMapper.toDto(hotel));

        restTemplate.put("/booking/update/booking/for/room/" + room.getId(), hotelMapper.toDto(hotel));
    }

    public List<Room> viewAvailableRooms(String name) {

        HotelDto hotelDto = restTemplate.getForObject("/hotel/find/by/name?name=" + name, HotelDto.class);
        Hotel hotel = hotelMapper.toEntity(hotelDto);

        List<Room> availableRooms = new ArrayList<>();
        if (hotel != null && hotel.getRooms() != null) {

            availableRooms = hotel.getRooms().stream()
                    .filter(Room::getIsAvailable)
                    .toList();

        }
        return availableRooms;
    }

    public List<Room> viewBookedRooms(String name) {

        HotelDto hotelDto = restTemplate.getForObject("/hotel/find/by/name?name=" + name, HotelDto.class);
        Hotel hotel = hotelMapper.toEntity(hotelDto);

        List<Room> bookedRooms = new ArrayList<>();
        if (hotel != null && hotel.getRooms() != null) {

            bookedRooms = hotel.getRooms().stream()
                    .filter(room -> !room.getIsAvailable())
                    .toList();

        }
        return bookedRooms;
    }

    public void bookingCancellation(String hotelName, String clientName) {

        HotelDto hotelDto = restTemplate.getForObject("/hotel/find/by/name?name=" + hotelName, HotelDto.class);
        Hotel hotel = hotelMapper.toEntity(hotelDto);

        if (hotel != null && hotel.getRooms() != null) {

            List<Room> bookedRooms = hotel.getRooms().stream()
                    .filter(room -> room.getBookings() != null && room.getBookings().stream()
                            .anyMatch(booking -> booking.getProfile() != null &&
                                    clientName.equals(booking.getProfile().getName())))
                    .toList();

            for (Room room : bookedRooms) {

                Booking bookingToCancel = room.getBookings().stream()
                        .filter(booking -> clientName.equals(booking.getProfile().getName()))
                        .findFirst()
                        .orElse(null);

                if (bookingToCancel != null) {

                    room.getBookings().remove(bookingToCancel);

                    room.setIsAvailable(true);

                    List<Room> rooms = new ArrayList<>();
                    rooms.add(room);
                    hotel.setRooms(rooms);

                    restTemplate.put("/room/update/booking/cancellation/" + room.getId(), hotelMapper.toDto(hotel));

                    long daysBooked = bookingToCancel.getCheckOutDate().toEpochDay() - bookingToCancel.getCheckInDate().toEpochDay();
                    double refundAmount = daysBooked * room.getPricePerNight();

                    Profile clientProfile = bookingToCancel.getProfile();
                    if (clientProfile != null) {

                        clientProfile.setBalance(clientProfile.getBalance() + refundAmount);

                        Notification notification = Notification.builder()
                                .message("Ваше бронирование номера №" + room.getRoomNumber() +
                                        " в отеле \"" + hotelName + "\" было отменено. Возврат: " + refundAmount + " рублей.")
                                .build();

                        clientProfile.getNotifications().add(notification);

                        restTemplate.put("/profile/update/" + clientProfile.getId(), profileMapper.toDto(clientProfile));
                    }
                }
                restTemplate.delete("/booking/delete/" + bookingToCancel.getId());
            }
        } else {
            throw new RuntimeException("Отель с названием \"" + hotelName + "\" не найден или не содержит номеров.");
        }
    }

    public void rateHotel(String hotelName, Double hotelEvaluation) {

        HotelDto hotelDto = restTemplate.getForObject("/hotel/find/by/name?name=" + hotelName, HotelDto.class);
        Hotel hotel = hotelMapper.toEntity(hotelDto);

        Double finalRating = hotel.getSocial().getRating();
        finalRating = (finalRating + hotelEvaluation)/2;
        hotel.getSocial().setRating(finalRating);

        restTemplate.put("/social/update/rate/hotel",hotelMapper.toDto(hotel),HotelDto.class);
    }

    public void bookmarkHotel(String hotelName, String login) {

        HotelDto hotelDto = restTemplate.getForObject("/hotel/find/by/name?name=" + hotelName, HotelDto.class);
        Hotel hotel = hotelMapper.toEntity(hotelDto);

        String url = "/profile/find?login=" + login;
        ResponseEntity<ProfileDto> response = restTemplate.getForEntity(url, ProfileDto.class);
        Profile profile = profileMapper.toEntity(response.getBody());

        restTemplate.postForObject("/profile/favorite/hotels?profileId="+profile.getId()+"&hotelId="+ hotel.getId(),null,String.class);

        hotel.getSocial().setFollowersCount(hotel.getSocial().getFollowersCount() + 1);
        restTemplate.put("/social/update/rate/hotel",hotelMapper.toDto(hotel));
    }

    public List<Hotel> seeAllHotelsInBookmarks(String login) {
        String url = "/profile/find?login=" + login;
        ResponseEntity<ProfileDto> response = restTemplate.getForEntity(url, ProfileDto.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            ProfileDto profileDto = response.getBody();

            Set<UUID> favoriteHotels = profileDto.getFavoriteHotels();
            List<HotelDto> hotels = new ArrayList<>();

            for (UUID hotelId : favoriteHotels) {
                try {
                    String urlHotel = "/hotel/read/" + hotelId.toString();
                    ResponseEntity<HotelDto> hotelResponse = restTemplate.getForEntity(urlHotel, HotelDto.class);

                    if (hotelResponse.getStatusCode().is2xxSuccessful() && hotelResponse.getBody() != null) {
                        hotels.add(hotelResponse.getBody());
                    }
                } catch (Exception e) {
                    System.err.println("Error retrieving hotel with ID: " + hotelId);
                }
            }

            return hotels.stream().map(hotelMapper::toEntity).toList();
        } else {
            throw new IllegalArgumentException("Profile not found for login: " + login);
        }
    }

    public void topUpBalance(String login, Double balance){

        restTemplate.put("/profile/top/up/balance?login="+login+"&balance="+balance, null, ProfileDto.class);

    }

    public void bookRoom(String login, String hotelName, Integer roomNumber, LocalDate checkInDate, LocalDate checkOutDate) {
        String url = String.format(
                "/booking/book/room?login=%s&hotelName=%s&roomNumber=%d&checkInDate=%s&checkOutDate=%s",
                login, hotelName, roomNumber, checkInDate.toString(), checkOutDate.toString()
        );
        restTemplate.postForObject(url, null, ProfileDto.class);
    }

}
