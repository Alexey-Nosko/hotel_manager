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
import by.ita.je.service.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebAppServiceTest extends TestUtils {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ProfileMapper profileMapper;

    @Mock
    private RoomMapper roomMapper;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private WebAppService webAppService;

    @Test
    void testClientRegistration() {

        Profile profile = new Profile();
        profile.setPassword("rawPassword");

        ProfileDto profileDto = new ProfileDto();
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode("rawPassword")).thenReturn(encodedPassword);
        when(profileMapper.toDto(profile)).thenReturn(profileDto);

        webAppService.clientRegistration(profile);

        Mockito.verify(passwordEncoder).encode("rawPassword");
        Mockito.verify(profileMapper).toDto(profile);
        Mockito.verify(restTemplate).postForObject(eq("/profile/create"), eq(profileDto), eq(ProfileDto.class));

        assertEquals(encodedPassword, profile.getPassword());
    }

    @Test
    void testPasswordReset() {

        String rawPassword = "newPassword";
        String encodedPassword = "encodedNewPassword";
        String currentUsername = "testUser";

        WebAppService webAppServiceSpy = Mockito.spy(webAppService);

        Mockito.doReturn(currentUsername).when(webAppServiceSpy).getCurrentUsername();
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        webAppServiceSpy.passwordReset(rawPassword);

        Mockito.verify(passwordEncoder).encode(rawPassword);
        Mockito.verify(restTemplate).postForObject(
                eq("/password/reset?login=" + currentUsername + "&password=" + encodedPassword),
                eq(null),
                eq(ProfileDto.class)
        );
    }

    @Test
    void filterHotels_happyPath() {

        HotelDto[] mockResponse = {new HotelDto(), new HotelDto()};
        when(restTemplate.getForObject(anyString(), eq(HotelDto[].class))).thenReturn(mockResponse);
        when(hotelMapper.toEntity(new HotelDto())).thenReturn(new Hotel());

        List<Hotel> result = webAppService.filterHotels(
                Optional.of("Grand Hotel"),
                Optional.of("New York"),
                Optional.of(4.5),
                Optional.of(true),
                Optional.of(true),
                Optional.of(true),
                Optional.of(true)
        );

        assertNotNull(result);
        assertEquals(2, result.size());
        Mockito.verify(restTemplate,  Mockito.times(1)).getForObject(
                eq("/hotels/filter?name=Grand Hotel&location=New York&minRating=4.5&wifi=true&pool=true&airConditioner=true&parking=true"),
                eq(HotelDto[].class)
        );
    }

    @Test
    void getSocialByHotelName() {
        UUID id = UUID.randomUUID();
        String name = "Grand Hotel";
        HotelDto hotelDto = buildHotelDto(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");

        when(restTemplate.getForObject("/hotel/find/by/name?name=" + name, HotelDto.class)).thenReturn(hotelDto);

        webAppService.getSocialByHotelName(name);

        Mockito.verify(restTemplate, Mockito.times(1)).getForObject("/hotel/find/by/name?name=Grand Hotel", HotelDto.class);
    }

    @Test
    void create() {

        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");
        HotelDto hotelDto = buildHotelDto(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");

        when(hotelMapper.toDto(hotel)).thenReturn(hotelDto);

        Hotel result = webAppService.create(hotel);

        assertNotNull(result);
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject(eq("/hotel/create"), eq(hotelDto), eq(HotelDto.class));
    }

    @Test
    void update() {

        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");
        HotelDto hotelDto = buildHotelDto(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");

        when(hotelMapper.toDto(hotel)).thenReturn(hotelDto);

        Hotel result = webAppService.update(hotel, "Grand Hotel");

        assertNotNull(result);
        Mockito.verify(restTemplate, Mockito.times(1)).postForObject("/hotel/update?name=Grand Hotel", hotelDto, HotelDto.class);
    }

    @Test
    void read() {

        UUID id = UUID.randomUUID();
        Hotel hotel = buildHotel(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");
        HotelDto hotelDto = buildHotelDto(id, "Sunset Resort", "Miami, FL", "A luxury hotel with stunning ocean views", "All year round");

        when(restTemplate.getForObject(anyString(), eq(HotelDto.class))).thenReturn(hotelDto);
        when(hotelMapper.toEntity(hotelDto)).thenReturn(hotel);

        Hotel result = webAppService.read("Grand Hotel");

        assertNotNull(result);
        Mockito.verify(restTemplate,  Mockito.times(1)).getForObject("/hotel/find/by/name?name=Grand Hotel", HotelDto.class);
    }

    @Test
    void delete() {
        webAppService.delete("Grand Hotel");

        Mockito.verify(restTemplate,  Mockito.times(1)).delete("/hotel/delete?name=Grand Hotel");
    }

    @Test
    public void testHotelManagerRegistration() {
        String hotelName = "TestHotel";
        Profile profile = new Profile();
        profile.setPassword("rawPassword");

        ProfileDto profileDto = new ProfileDto();
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(profileMapper.toDto(profile)).thenReturn(profileDto);

        webAppService.hotelManagerRegistration(hotelName, profile);

        Mockito.verify(passwordEncoder).encode("rawPassword");
        Mockito.verify(restTemplate).postForObject(eq("/manager/registration?name=" + hotelName), eq(profileDto), eq(ProfileDto.class));
    }

    @Test
    public void testChangeHotelRoomConfiguration() {
        String hotelName = "TestHotel";
        Room room = new Room();
        RoomDto roomDto = new RoomDto();

        when(roomMapper.toDto(room)).thenReturn(roomDto);

        webAppService.changeHotelRoomConfiguration(hotelName, room);

        Mockito.verify(restTemplate).postForObject(eq("/change/configuration?name=" + hotelName), eq(roomDto), eq(RoomDto.class));
    }

    @Test
    public void testViewAvailableRooms() {
        String hotelName = "TestHotel";
        RoomDto roomDto1 = new RoomDto();
        RoomDto roomDto2 = new RoomDto();
        Room room1 = new Room();
        Room room2 = new Room();
        List<RoomDto> roomDtoList = Arrays.asList(roomDto1, roomDto2);

        ParameterizedTypeReference<List<RoomDto>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<RoomDto>> responseEntity = ResponseEntity.ok(roomDtoList);

        when(restTemplate.exchange(
                eq("/hotel/view/available/rooms?name=" + hotelName),
                eq(HttpMethod.GET),
                eq(null),
                eq(responseType))
        ).thenReturn(responseEntity);

        when(roomMapper.toEntity(roomDto1)).thenReturn(room1);
        when(roomMapper.toEntity(roomDto2)).thenReturn(room2);

        List<Room> availableRooms = webAppService.viewAvailableRooms(hotelName);

        assertEquals(2, availableRooms.size());
        assertTrue(availableRooms.contains(room1));
        assertTrue(availableRooms.contains(room2));
    }


    @Test
    public void testViewBookedRooms() {

        String hotelName = "TestHotel";
        RoomDto roomDto1 = new RoomDto();
        RoomDto roomDto2 = new RoomDto();
        Room room1 = new Room();
        Room room2 = new Room();
        List<RoomDto> roomDtoList = Arrays.asList(roomDto1, roomDto2);

        ParameterizedTypeReference<List<RoomDto>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<RoomDto>> responseEntity = ResponseEntity.ok(roomDtoList);

        when(restTemplate.exchange(
                eq("/hotel/view/booked/rooms?name=" + hotelName),
                eq(HttpMethod.GET),
                eq(null),
                eq(responseType))
        ).thenReturn(responseEntity);

        when(roomMapper.toEntity(roomDto1)).thenReturn(room1);
        when(roomMapper.toEntity(roomDto2)).thenReturn(room2);

        List<Room> bookedRooms = webAppService.viewBookedRooms(hotelName);

        assertEquals(2, bookedRooms.size());
        assertTrue(bookedRooms.contains(room1));
        assertTrue(bookedRooms.contains(room2));
    }

    @Test
    public void testBookingCancellation() {
        String hotelName = "TestHotel";
        String clientName = "TestClient";

        webAppService.bookingCancellation(hotelName, clientName);

        Mockito.verify(restTemplate).postForObject(eq("/booking/cancellation?name=" + hotelName + "&clientName=" + clientName), eq(null), eq(HotelDto.class));
    }

    @Test
    void rateHotel() {
        String name = "Test Hotel";
        Double hotelEvaluation = 4.5;

        when(restTemplate.postForObject(eq("/rate/hotel?name=" + name + "&hotelEvaluation=" + hotelEvaluation), eq(null), eq(HotelDto.class))).thenReturn(new HotelDto());

        webAppService.rateHotel(name, hotelEvaluation);

        Mockito.verify(restTemplate).postForObject(eq("/rate/hotel?name=Test Hotel&hotelEvaluation=4.5"), eq(null), eq(HotelDto.class));
    }

    @Test
    void getCurrentUsername() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("testUser");
        when(authentication.getName()).thenReturn("testUser");

        String username = webAppService.getCurrentUsername();

        assertEquals("testUser", username);
    }

    @Test
    void bookmarkHotel() {
        String name = "Test Hotel";
        String login = "Test login";

        WebAppService spyWebAppService = Mockito.spy(webAppService);

        when(spyWebAppService.getCurrentUsername()).thenReturn(login);

        when(restTemplate.postForObject(eq("/add/bookmark?name=" + name + "&login=" + login), eq(null), eq(HotelDto.class)))
                .thenReturn(new HotelDto());

        spyWebAppService.bookmarkHotel(name);

        Mockito.verify(restTemplate).postForObject(eq("/add/bookmark?name=" + name + "&login=" + login), eq(null), eq(HotelDto.class));
    }


    @Test
    void seeAllHotelsInBookmarks() {
        String username = "testUser";

        WebAppService spyWebAppService = Mockito.spy(webAppService);

        when(spyWebAppService.getCurrentUsername()).thenReturn(username);

        HotelDto hotelDto = new HotelDto();
        when(restTemplate.getForEntity("/see/all/hotels/in/bookmarks?login=" + username, HotelDto[].class))
                .thenReturn(ResponseEntity.ok(new HotelDto[]{hotelDto}));
        when(hotelMapper.toEntity(hotelDto)).thenReturn(new Hotel());

        List<Hotel> hotels = spyWebAppService.seeAllHotelsInBookmarks();

        assertEquals(1, hotels.size());

        Mockito.verify(restTemplate).getForEntity(eq("/see/all/hotels/in/bookmarks?login=testUser"), eq(HotelDto[].class));
    }


    @Test
    void getProfile() {
        String username = "testUser";

        WebAppService spyWebAppService = Mockito.spy(webAppService);

        when(spyWebAppService.getCurrentUsername()).thenReturn(username);

        ProfileDto profileDto = new ProfileDto();
        when(restTemplate.getForEntity("/find?login=" + username, ProfileDto.class))
                .thenReturn(ResponseEntity.ok(profileDto));
        when(profileMapper.toEntity(profileDto)).thenReturn(new Profile());

        Profile profile = spyWebAppService.getProfile();

        assertNotNull(profile);

        Mockito.verify(restTemplate).getForEntity(eq("/find?login=testUser"), eq(ProfileDto.class));
    }

    @Test
    void topUpBalance() {
        String username = "testUser";
        Double balance = 100.0;

        WebAppService spyWebAppService = Mockito.spy(webAppService);

        when(spyWebAppService.getCurrentUsername()).thenReturn(username);

        when(restTemplate.postForObject(anyString(), eq(null), eq(ProfileDto.class)))
                .thenReturn(new ProfileDto());

        spyWebAppService.topUpBalance(balance);

        Mockito.verify(restTemplate).postForObject(eq("/top/up/balance?login=testUser&balance=100.0"), eq(null), eq(ProfileDto.class));
    }

    @Test
    void bookRoom() {
        String username = "testUser";
        String hotelName = "Test Hotel";
        Integer roomNumber = 101;
        LocalDate checkInDate = LocalDate.of(2024, 12, 20);
        LocalDate checkOutDate = LocalDate.of(2024, 12, 25);

        when(webAppService.getCurrentUsername()).thenReturn(username);
        when(restTemplate.postForObject(anyString(), eq(null), eq(ProfileDto.class))).thenReturn(new ProfileDto());

        webAppService.bookRoom(hotelName, roomNumber, checkInDate, checkOutDate);

        Mockito.verify(restTemplate).postForObject(eq("/book/room?login=testUser&hotelName=Test Hotel&roomNumber=101&checkInDate=2024-12-20&checkOutDate=2024-12-25"), eq(null), eq(ProfileDto.class));
    }
}