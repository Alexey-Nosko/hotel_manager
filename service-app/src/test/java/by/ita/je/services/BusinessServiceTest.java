package by.ita.je.services;

import by.ita.je.dto.HotelDto;
import by.ita.je.dto.ProfileDto;
import by.ita.je.dto.RoomDto;
import by.ita.je.dto.SocialDto;
import by.ita.je.mappers.HotelMapper;
import by.ita.je.mappers.ProfileMapper;
import by.ita.je.models.Hotel;
import by.ita.je.models.Profile;
import by.ita.je.models.Room;
import by.ita.je.models.Social;
import by.ita.je.services.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessServiceTest extends TestUtils {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HotelMapper hotelMapper;

    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private BusinessService businessService;

    @Test
    void testCreateHotel() {
        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");
        HotelDto hotelDto = buildHotelDto(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");

        when(hotelMapper.toDto(hotel)).thenReturn(hotelDto);

        businessService.createHotel(hotel);

        verify(restTemplate,  Mockito.times(1)).postForObject("/hotel/create", hotelDto, HotelDto.class);
    }

    @Test
    void testUpdateHotel() {

        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");
        HotelDto hotelDto = buildHotelDto(id,"Sunset Resort","Miami, FL","A luxury hotel with stunning ocean views","All year round");
        String name = "Sunset Resort";

        when(restTemplate.getForObject("/hotel/find/by/name?name=" + name, HotelDto.class)).thenReturn(hotelDto);
        when(hotelMapper.toEntity(hotelDto)).thenReturn(hotel);
        when(hotelMapper.toDto(hotel)).thenReturn(hotelDto);

        businessService.updateHotel(name, hotel);

        verify(restTemplate,  Mockito.times(1)).getForObject("/hotel/find/by/name?name=" + name, HotelDto.class);
        verify(restTemplate,  Mockito.times(1)).put("/hotel/update/by/name?name=" + name, hotelDto, HotelDto.class);
    }

    @Test
    void testDeleteHotel() {
        String name = "Sunset Resort";
        businessService.deleteHotel(name);

        verify(restTemplate,  Mockito.times(1)).delete("/hotel/delete/by/name?name="+name);
    }

    @Test
    void testClientRegistration() {

        UUID id = UUID.randomUUID();
        Profile profile = buildProfile(id,"client","Mark","login","password",200.0);
        ProfileDto profileDto = buildProfileDto(id,"client","Mark","login","password",200.0);


        when(profileMapper.toDto(profile)).thenReturn(profileDto);

        businessService.clientRegistration(profile);

        verify(restTemplate).postForObject("/profile/create", profileDto, ProfileDto.class);
    }

    @Test
    void testPasswordReset() {
        String login = "testUser";
        String password = "newPassword";

        businessService.passwordReset(login, password);

        verify(restTemplate).put("/profile/password/reset?login=" + login + "&password=" + password, null, ProfileDto.class);
    }

    @Test
    void testFindByLoginSuccess() {
        String login = "testUser";
        ProfileDto profileDto = new ProfileDto();
        ResponseEntity<ProfileDto> responseEntity = mock(ResponseEntity.class);

        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(responseEntity.getBody()).thenReturn(profileDto);
        when(restTemplate.getForEntity(eq("/profile/find?login=" + login), eq(ProfileDto.class))).thenReturn(responseEntity);

        ProfileDto result = businessService.findByLogin(login);

        assertEquals(profileDto, result);
    }

    @Test
    void testFilterHotels_buildCorrectUrl() {

        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");
        HotelDto hotelDto = buildHotelDto(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");
        HotelDto[] mockResponse = {hotelDto, hotelDto};

        when(restTemplate.getForObject(contains("/hotel/hotels/filter"), eq(HotelDto[].class))).thenReturn(mockResponse);
        when(hotelMapper.toEntity(hotelDto)).thenReturn(hotel);

        List<Hotel> hotels = businessService.filterHotels(
                Optional.of("Sunset Resort"),
                Optional.of("Miami, FL"),
                Optional.of(4.5),
                Optional.of(true),
                Optional.of(false),
                Optional.of(true),
                Optional.of(false)
        );

        ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        verify(restTemplate).getForObject(urlCaptor.capture(), eq(HotelDto[].class));

        String actualUrl = urlCaptor.getValue();
        assertTrue(actualUrl.contains("name=Sunset Resort"));
        assertTrue(actualUrl.contains("location=Miami, FL"));
        assertTrue(actualUrl.contains("minRating=4.5"));
        assertTrue(actualUrl.contains("wifi=true"));
        assertTrue(actualUrl.contains("pool=false"));
        assertTrue(actualUrl.contains("airConditioner=true"));
        assertTrue(actualUrl.contains("parking=false"));

        assertNotNull(hotels);
        assertEquals(2, hotels.size());
        assertEquals("Sunset Resort", hotels.get(0).getName());
    }

    @Test
    void testFindHotelByName() {
        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");
        HotelDto hotelDto = buildHotelDto(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");

        when(hotelMapper.toEntity(hotelDto)).thenReturn(hotel);
        when(restTemplate.getForObject(eq("/hotel/find/by/name?name=Hotel1"), eq(HotelDto.class))).thenReturn(hotelDto);

        Hotel result = businessService.findHotelByName("Hotel1");

        verify(restTemplate).getForObject(eq("/hotel/find/by/name?name=Hotel1"), eq(HotelDto.class));
        assertNotNull(result);
        assertEquals("Sunset Resort", result.getName());
    }

    @Test
    void viewAvailableRooms() {

        String hotelName = "TestHotel";
        HotelDto hotelDto = new HotelDto();
        Hotel hotel = new Hotel();
        Room room1 = Room.builder()
                .roomNumber(1)
                .isAvailable(true)
                .build();
        Room room2 = Room.builder()
                .roomNumber(2)
                .isAvailable(false)
                .build();

        hotel.setRooms(List.of(room1, room2));

        when(restTemplate.getForObject("/hotel/find/by/name?name=" + hotelName, HotelDto.class)).thenReturn(hotelDto);
        when(hotelMapper.toEntity(hotelDto)).thenReturn(hotel);


        List<Room> availableRooms = businessService.viewAvailableRooms(hotelName);

        assertEquals(1, availableRooms.size());
        assertTrue(availableRooms.contains(room1));
    }

    @Test
    void viewBookedRooms() {

        String hotelName = "TestHotel";
        HotelDto hotelDto = new HotelDto();
        Hotel hotel = new Hotel();
        Room room1 = Room.builder()
                .roomNumber(1)
                .isAvailable(true)
                .build();
        Room room2 = Room.builder()
                .roomNumber(2)
                .isAvailable(false)
                .build();
        hotel.setRooms(List.of(room1, room2));

        when(restTemplate.getForObject("/hotel/find/by/name?name=" + hotelName, HotelDto.class)).thenReturn(hotelDto);
        when(hotelMapper.toEntity(hotelDto)).thenReturn(hotel);

        List<Room> bookedRooms = businessService.viewBookedRooms(hotelName);

        assertEquals(1, bookedRooms.size());
        assertTrue(bookedRooms.contains(room2));
    }

    @Test
    void testHotelManagerRegistration() {
        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");
        HotelDto hotelDto = buildHotelDto(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");

        Profile profile = new Profile();
        profile.setId(id);

        hotelDto.setProfilesDto(new ArrayList<>());

        hotel.setProfiles(new ArrayList<>());

        when(restTemplate.getForObject("/hotel/find/by/name?name=" + hotelDto.getName(), HotelDto.class)).thenReturn(hotelDto);
        when(hotelMapper.toEntity(hotelDto)).thenReturn(hotel);
        when(hotelMapper.toDto(hotel)).thenReturn(hotelDto);

        businessService.hotelManagerRegistration(hotelDto.getName(), profile);

        assertTrue(hotel.getProfiles().contains(profile));
        verify(restTemplate).postForObject("/profile/manager/registration", hotelDto, HotelDto.class);
        verify(restTemplate).put("/hotel/update/by/name?name=" + hotelDto.getName(), hotelDto, HotelDto.class);
    }

    @Test
    void testChangeHotelRoomConfiguration() {

        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");
        HotelDto hotelDto = buildHotelDto(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");

        String hotelName = "TestHotel";
        Room adventRoom = new Room();
        adventRoom.setRoomNumber(101);
        adventRoom.setType("Deluxe");
        adventRoom.setPricePerNight(200);

        RoomDto roomDto = new RoomDto();
        roomDto.setRoomNumber(101);
        roomDto.setType("Single");
        roomDto.setPricePerNight(100);
        hotelDto.setRoomsDto(List.of(roomDto));

        Room room = new Room();
        room.setRoomNumber(101);
        room.setType("Single");
        room.setPricePerNight(100);
        hotel.setRooms(List.of(room));

        when(restTemplate.getForObject("/hotel/find/by/name?name=" + hotelName, HotelDto.class)).thenReturn(hotelDto);
        when(hotelMapper.toEntity(hotelDto)).thenReturn(hotel);
        when(hotelMapper.toDto(hotel)).thenReturn(hotelDto);

        businessService.changeHotelRoomConfiguration(hotelName, adventRoom);

        assertEquals("Deluxe", room.getType());
        assertEquals(200, room.getPricePerNight());
        verify(restTemplate).put("/room/update/change/configuration/" + room.getId(), hotelDto);
        verify(restTemplate).put("/booking/update/booking/for/room/" + room.getId(), hotelDto);
    }

    @Test
    void rateHotel() {

        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");
        HotelDto hotelDto = buildHotelDto(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");

        String hotelName = "Test Hotel";
        Double hotelEvaluation = 4.0;

        SocialDto socialDto = SocialDto.builder()
                .rating(3.0)
                .build();
        hotelDto.setSocialDto(socialDto);
        Social social = Social.builder()
                .rating(3.0)
                .build();
        hotel.setSocial(social);

        when(restTemplate.getForObject(anyString(), eq(HotelDto.class))).thenReturn(hotelDto);
        when(hotelMapper.toEntity(hotelDto)).thenReturn(hotel);
        when(hotelMapper.toDto(hotel)).thenReturn(hotelDto);

        businessService.rateHotel(hotelName, hotelEvaluation);

        verify(restTemplate).put(eq("/social/update/rate/hotel"), eq(hotelDto));
        assertEquals(3.5, hotel.getSocial().getRating());
    }

    @Test
    void bookmarkHotel() {
        String hotelName = "Test Hotel";
        String login = "testUser";

        HotelDto hotelDto = new HotelDto();
        hotelDto.setId(UUID.randomUUID());
        Hotel hotel = new Hotel();
        hotel.setId(hotelDto.getId());

        Social social = new Social();
        social.setFollowersCount(0);
        hotel.setSocial(social);

        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(UUID.randomUUID());
        Profile profile = new Profile();
        profile.setId(profileDto.getId());

        when(restTemplate.getForObject(eq("/hotel/find/by/name?name=" + hotelName), eq(HotelDto.class))).thenReturn(hotelDto);
        when(hotelMapper.toEntity(hotelDto)).thenReturn(hotel);
        String url = "/profile/find?login=" + login;

        when(restTemplate.getForEntity(eq(url), eq(ProfileDto.class)))
                .thenReturn(new ResponseEntity<>(profileDto, HttpStatus.OK));
        when(profileMapper.toEntity(profileDto)).thenReturn(profile);

        businessService.bookmarkHotel(hotelName, login);

        verify(restTemplate).postForObject(
                eq("/profile/favorite/hotels?profileId=" + profile.getId() + "&hotelId=" + hotel.getId()),
                isNull(),
                eq(String.class)
        );

        assertEquals(1, hotel.getSocial().getFollowersCount());
    }

    @Test
    void seeAllHotelsInBookmarks() {
        String login = "testUser";

        ProfileDto profileDto = new ProfileDto();
        profileDto.setFavoriteHotels(Set.of(UUID.randomUUID()));
        HotelDto hotelDto = new HotelDto();
        Hotel hotel = new Hotel();

        String url = "/profile/find?login=" + login;

        when(restTemplate.getForEntity(eq(url), eq(ProfileDto.class))).thenReturn(new ResponseEntity<>(profileDto, HttpStatus.OK));
        when(restTemplate.getForEntity(eq(url), eq(HotelDto.class))).thenReturn(new ResponseEntity<>(hotelDto, HttpStatus.OK));
        when(hotelMapper.toEntity(hotelDto)).thenReturn(hotel);

        List<Hotel> result = businessService.seeAllHotelsInBookmarks(login);

        assertEquals(0, result.size());
    }

    @Test
    void topUpBalance() {
        String login = "testUser";
        Double balance = 100.0;

        businessService.topUpBalance(login, balance);

        verify(restTemplate).put(eq("/profile/top/up/balance?login=" + login + "&balance=" + balance), isNull(), eq(ProfileDto.class));
    }

    @Test
    void bookRoom() {
        String login = "testUser";
        String hotelName = "Test Hotel";
        Integer roomNumber = 101;
        LocalDate checkInDate = LocalDate.now();
        LocalDate checkOutDate = LocalDate.now().plusDays(2);

        businessService.bookRoom(login, hotelName, roomNumber, checkInDate, checkOutDate);

        verify(restTemplate).postForObject(
                eq(String.format(
                        "/booking/book/room?login=%s&hotelName=%s&roomNumber=%d&checkInDate=%s&checkOutDate=%s",
                        login, hotelName, roomNumber, checkInDate, checkOutDate
                )),
                isNull(), eq(ProfileDto.class)
        );
    }
}