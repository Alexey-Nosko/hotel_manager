package by.ita.je.controller;

import by.ita.je.models.*;
import by.ita.je.service.WebAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/hotel")
@RequiredArgsConstructor
public class WebAppController {

    private final WebAppService webAppService;

    @GetMapping("/start")
    public String showHomePage() {
        return "index";
    }

    @GetMapping("/filter")
    public String showfilters() {
        return "filter";
    }

    @PostMapping("/hotels/filter")
    public String filterHotels(
            @RequestParam Optional<String> name,
            @RequestParam Optional<String> location,
            @RequestParam Optional<Double> minRating,
            @RequestParam Optional<Boolean> wifi,
            @RequestParam Optional<Boolean> pool,
            @RequestParam Optional<Boolean> airConditioner,
            @RequestParam Optional<Boolean> parking,
            Model model
    ) {
        List<Hotel> hotels =  webAppService.filterHotels(name, location, minRating, wifi, pool, airConditioner, parking);

        model.addAttribute("hotels", hotels);

        return "hotelResults";
    }

    @GetMapping("/social")
    public String social() {
        return "getSocial";
    }

    @GetMapping("/get/social")
    public String showSocialInfo(@RequestParam String name, Model model) {
        Hotel hotel = webAppService.getSocialByHotelName(name);

        if (hotel != null) {
            model.addAttribute("hotel", hotel);
            return "social";
        } else {
            model.addAttribute("error", "Hotel not found!");
            return "index";
        }
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("hotel", new Hotel());
        return "create";
    }

    @PostMapping("/create")
    public String createHotel(@ModelAttribute("hotel") Hotel hotel, Model model) {

        if (hotel.getAmenities() == null) {
            hotel.setAmenities(new Amenities());
        }
        if (hotel.getAmenities().getWifi() == null) hotel.getAmenities().setWifi(false);
        if (hotel.getAmenities().getPool() == null) hotel.getAmenities().setPool(false);
        if (hotel.getAmenities().getAirConditioner() == null) hotel.getAmenities().setAirConditioner(false);
        if (hotel.getAmenities().getParking() == null) hotel.getAmenities().setParking(false);

        Hotel hotel1 = webAppService.create(hotel);

        model.addAttribute("hotel", hotel1);
        return "createSuccess";
    }

    @GetMapping("/read")
    public String read() {
        return "read";
    }

    @GetMapping("/get/read")
    public String showHotelInfo(@RequestParam String name, Model model) {
        Hotel hotel = webAppService.read(name);


        if (hotel != null) {
            model.addAttribute("hotel", hotel);
            return "readSuccess";
        } else {
            model.addAttribute("error", "Hotel not found!");
            return "index";
        }
    }

    @GetMapping("/update")
    public String updateForm(Model model) {
        model.addAttribute("hotel", new Hotel());
        return "update";
    }

    @PostMapping("/update")
    public String updateHotel(@ModelAttribute("hotel") Hotel hotel, String name, Model model) {


        if (hotel.getAmenities() == null) {
            hotel.setAmenities(new Amenities());
        }
        if (hotel.getAmenities().getWifi() == null) hotel.getAmenities().setWifi(false);
        if (hotel.getAmenities().getPool() == null) hotel.getAmenities().setPool(false);
        if (hotel.getAmenities().getAirConditioner() == null) hotel.getAmenities().setAirConditioner(false);
        if (hotel.getAmenities().getParking() == null) hotel.getAmenities().setParking(false);


        Hotel hotel1 = webAppService.update(hotel,name);

        model.addAttribute("hotel", hotel1);
        return "updateSuccess";
    }

    @GetMapping("/delete")
    public String delete() {
        return "delete";
    }

    @GetMapping("/get/delete")
    public String deleteSuccess(@RequestParam String name, Model model) {
        webAppService.delete(name);

        model.addAttribute("hotel");
        return "deleteSuccess";
    }

    @GetMapping("/manager/registration")
    public String managerRegistration(Model model) {
        model.addAttribute("hotel", new Profile());
        return "managerRegistration";
    }

    @PostMapping("/manager/registration")
    public String managerRegistration(@ModelAttribute("profile") Profile profile, String hotelName, Model model) {

        webAppService.hotelManagerRegistration(hotelName,profile);

        return "managerRegistrationSuccess";
    }

    @GetMapping("/change/configuration")
    public String changeHotelRoomConfiguration() {
        return "ChangeHotelRoomConfiguration";
    }

    @GetMapping("/get/change/configuration")
    public String getChangeHotelRoomConfiguration(@RequestParam String name,
                                                  @ModelAttribute Room room) {
        webAppService.changeHotelRoomConfiguration(name, room);
        return "ChangeHotelRoomConfigurationSuccess";
    }


    @GetMapping("/view/available/rooms")
    public String viewAvailableRooms() {
        return "viewAvailableRooms";
    }

    @GetMapping("/get/view/available/rooms")
    public String getViewAvailableRooms(@RequestParam String name, Model model) {

        List<Room> rooms = webAppService.viewAvailableRooms(name);

        if (rooms != null) {
            model.addAttribute("rooms", rooms);
            return "ViewAvailableRoomsSuccess";
        } else {
            model.addAttribute("error", "Hotel not found!");
            return "index";
        }
    }

    @GetMapping("/view/booked/rooms")
    public String viewBookedRooms() {
        return "viewBookedRooms";
    }

    @GetMapping("/get/view/booked/rooms")
    public String getViewBookedRooms(@RequestParam String name, Model model) {

        List<Room> rooms = webAppService.viewBookedRooms(name);

        if (rooms != null) {
            model.addAttribute("rooms", rooms);
            return "viewBookedRoomsSuccess";
        } else {
            model.addAttribute("error", "Hotel not found!");
            return "index";
        }
    }

    @GetMapping("/booking/cancellation")
    public String bookingCancellation() {
        return "bookingCancellation";
    }

    @GetMapping("/get/booking/Cancellation")
    public String getBookingCancellation(@RequestParam String name,@RequestParam String clientName) {

        webAppService.bookingCancellation(name,clientName);

        return "bookingCancellationSuccess";
    }

    @GetMapping("/rate/hotel")
    public String rateHotel() {
        return "rateHotel";
    }

    @GetMapping("/get/rate/hotel")
    public String getRateHotel(@RequestParam String name,@RequestParam Double hotelEvaluation) {

        webAppService.rateHotel(name,hotelEvaluation);

        return "rateHotelSuccess";
    }

    @GetMapping("/add/bookmark")
    public String bookmarkHotel() {
        return "bookmarkHotel";
    }

    @GetMapping("/get/add/bookmark")
    public String getBookmarkHotel(@RequestParam String name) {

        webAppService.bookmarkHotel(name);

        return "bookmarkHotelSuccess";
    }

    @GetMapping("/see/all/hotels/in/bookmarks")
    public String seeAllHotelsInBookmarks(Model model) {
        List<Hotel> hotels =  webAppService.seeAllHotelsInBookmarks();

        model.addAttribute("hotels", hotels);

        return "bookmarks";
    }

    @GetMapping("/see/profile")
    public String getProfile(Model model) {
        Profile profile =  webAppService.getProfile();

        model.addAttribute("profile", profile);

        return "getProfile";
    }

    @GetMapping("/password/reset")
    public String passwordReset() {
        return "passwordReset";
    }

    @GetMapping("/get/password/reset")
    public String getPasswordReset(@RequestParam String password) {

        webAppService.passwordReset(password);

        return "passwordResetSuccess";
    }

    @GetMapping("/top/up/balance")
    public String topUpBalance() {
        return "topUpBalance";
    }

    @GetMapping("/get/top/up/balance")
    public String topUpBalance(@RequestParam Double balance) {

        webAppService.topUpBalance(balance);

        return "topUpBalanceSuccess";
    }

    @GetMapping("/book/room")
    public String bookRoom() {
        return "bookRoom";
    }

    @GetMapping("/get/book/room")
    public String bookRoom(@RequestParam String hotelName,
                           @RequestParam Integer roomNumber, @RequestParam LocalDate checkInDate,
                           @RequestParam LocalDate checkOutDate) {

        webAppService.bookRoom(hotelName,roomNumber,checkInDate,checkOutDate);

        return "bookRoomSuccess";
    }

}
