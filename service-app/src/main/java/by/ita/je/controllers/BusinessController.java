package by.ita.je.controllers;

import by.ita.je.dto.HotelDto;
import by.ita.je.dto.ProfileDto;
import by.ita.je.dto.RoomDto;
import by.ita.je.mappers.HotelMapper;
import by.ita.je.mappers.ProfileMapper;
import by.ita.je.mappers.RoomMapper;
import by.ita.je.services.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;
    private final HotelMapper hotelMapper;
    private final ProfileMapper profileMapper;
    private final RoomMapper roomMapper;

    @PostMapping("/profile/create")
    public void clientRegistration(@RequestBody ProfileDto profileDto){
        businessService.clientRegistration(profileMapper.toEntity(profileDto));
    }

    @PostMapping("/password/reset")
    public void passwordReset(@RequestParam String login, @RequestParam String password){
        businessService.passwordReset(login,password);
    }

    @GetMapping("/find")
    public ResponseEntity<ProfileDto> findByLogin(@RequestParam String login) {
        try {
            ProfileDto profile = businessService.findByLogin(login);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

   @GetMapping("/hotels/filter")
   public List<HotelDto> filterHotels(
           @RequestParam Optional<String> name,
           @RequestParam Optional<String> location,
           @RequestParam Optional<Double> minRating,
           @RequestParam Optional<Boolean> wifi,
           @RequestParam Optional<Boolean> pool,
           @RequestParam Optional<Boolean> airConditioner,
           @RequestParam Optional<Boolean> parking
   ){
       return businessService.filterHotels(name, location, minRating, wifi, pool,airConditioner,parking)
               .stream()
               .map(hotelMapper::toDto)
               .toList();
   }

   @GetMapping("/hotel/find/by/name")
   public HotelDto findHotelByName(@RequestParam String name){

       return hotelMapper.toDto(businessService.findHotelByName(name));
   }

    @PostMapping("/hotel/create")
    public HotelDto createLaptop(@RequestBody HotelDto hotelDto){

        return hotelMapper.toDto(businessService.createHotel(hotelMapper.toEntity(hotelDto)));
    }

    @PostMapping("/hotel/update")
    public HotelDto updateHotel(@RequestParam String name, @RequestBody HotelDto hotelDto){

        return hotelMapper.toDto(businessService.updateHotel(name,hotelMapper.toEntity(hotelDto)));
    }

    @DeleteMapping("/hotel/delete")
    public void delete(@RequestParam String name)
    {
        businessService.deleteHotel(name);
    }

    @PostMapping("/manager/registration")
    public void hotelManagerRegistration(@RequestParam String name, @RequestBody ProfileDto profileDto){

         businessService.hotelManagerRegistration(name,profileMapper.toEntity(profileDto));
    }

    @PostMapping("/change/configuration")
    public void changeHotelRoomConfiguration(@RequestParam String name,
                                             @RequestBody RoomDto roomDto){

       businessService.changeHotelRoomConfiguration(name,roomMapper.toEntity(roomDto));
    }

    @GetMapping("/hotel/view/available/rooms")
    public List<RoomDto> viewAvailableRooms(@RequestParam String name){

        return businessService.viewAvailableRooms(name).stream()
                .map(roomMapper::toDto)
                .toList();
    }

    @GetMapping("/hotel/view/booked/rooms")
    public List<RoomDto> viewBookedRooms(@RequestParam String name){

        return businessService.viewBookedRooms(name).stream()
                .map(roomMapper::toDto)
                .toList();
    }

    @PostMapping("/booking/cancellation")
    public void bookingCancellation(@RequestParam String name, @RequestParam String clientName){

       businessService.bookingCancellation(name,clientName);
    }

    @PostMapping("/rate/hotel")
    public void rateHotel(@RequestParam String name, @RequestParam Double hotelEvaluation){

        businessService.rateHotel(name,hotelEvaluation);
    }

    @PostMapping("/add/bookmark")
    public void bookmarkHotel(@RequestParam String name, @RequestParam String login){

        businessService.bookmarkHotel(name,login);
    }

    @GetMapping("/see/all/hotels/in/bookmarks")
    public List<HotelDto> seeAllHotelsInBookmarks(@RequestParam String login){

      return businessService.seeAllHotelsInBookmarks(login).stream()
              .map(hotelMapper::toDto).toList();
    }

    @PostMapping("/top/up/balance")
    public void topUpBalance(@RequestParam String login, @RequestParam Double balance){
        businessService.topUpBalance(login,balance);
    }

    @PostMapping("/book/room")
    public void bookRoom(@RequestParam String login,@RequestParam String hotelName,
                         @RequestParam Integer roomNumber, @RequestParam LocalDate checkInDate,
                         @RequestParam LocalDate checkOutDate){
        businessService.bookRoom(login, hotelName, roomNumber, checkInDate, checkOutDate);
    }
}
