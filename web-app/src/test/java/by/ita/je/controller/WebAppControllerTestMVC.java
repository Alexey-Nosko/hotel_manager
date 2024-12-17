package by.ita.je.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
class WebAppControllerTestMVC {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowHomePage() throws Exception {
        mockMvc.perform(get("/hotel/start"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testShowFiltersPage() throws Exception {
        mockMvc.perform(get("/hotel/filter"))
                .andExpect(status().isOk())
                .andExpect(view().name("filter"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testFilterHotels() throws Exception {
        mockMvc.perform(post("/hotel/hotels/filter")
                        .param("name", "Test Hotel")
                        .param("location", "Test Location")
                        .param("minRating", "4.5")
                        .param("wifi", "true")
                        .param("pool", "false")
                        .param("airConditioner", "true")
                        .param("parking", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("hotelResults"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testCreateHotelForm() throws Exception {
        mockMvc.perform(get("/hotel/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testCreateHotel() throws Exception {
        mockMvc.perform(post("/hotel/create")
                        .param("name", "New Hotel")
                        .param("location", "New Location")
                        .param("description", "A beautiful new hotel.")
                        .param("periodOfWork", "All year")
                        .param("amenities.wifi", "true")
                        .param("amenities.pool", "true")
                        .param("amenities.airConditioner", "false")
                        .param("amenities.parking", "true")
                        .param("social.rating", "5.0")
                        .param("social.followersCount", "0")
                        .param("rooms[0].roomNumber", "101")
                        .param("rooms[0].type", "Single")
                        .param("rooms[0].pricePerNight", "100")
                        .param("rooms[0].isAvailable", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("createSuccess"));
    }


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testReadHotelForm() throws Exception {
        mockMvc.perform(get("/hotel/read"))
                .andExpect(status().isOk())
                .andExpect(view().name("read"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testReadHotelInfo() throws Exception {
        mockMvc.perform(get("/hotel/get/read")
                        .param("name", "Marriott"))
                .andExpect(status().isOk())
                .andExpect(view().name("readSuccess"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUpdateHotelForm() throws Exception {
        mockMvc.perform(get("/hotel/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("update"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testUpdateHotel() throws Exception {
        mockMvc.perform(post("/hotel/update")
                        .param("name", "Sunset Resort")
                        .param("location", "Updated Location"))
                .andExpect(status().isOk())
                .andExpect(view().name("updateSuccess"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteHotelForm() throws Exception {
        mockMvc.perform(get("/hotel/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("delete"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testDeleteHotel() throws Exception {
        mockMvc.perform(get("/hotel/get/delete")
                        .param("name", "Hotel to Delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("deleteSuccess"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testManagerRegistrationForm() throws Exception {
        mockMvc.perform(get("/hotel/manager/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("managerRegistration"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testManagerRegistration() throws Exception {
        mockMvc.perform(post("/hotel/manager/registration")
                        .param("hotelName", "Sunset Resort")
                        .param("name", "Max")
                        .param("login", "log")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(view().name("managerRegistrationSuccess"));
    }

    @Test
    @WithMockUser(username = "manag", roles = "MANAGER")
    void testChangeHotelRoomConfiguration() throws Exception {
        mockMvc.perform(get("/hotel/change/configuration"))
                .andExpect(status().isOk())
                .andExpect(view().name("ChangeHotelRoomConfiguration"));
    }

    @Test
    @WithMockUser(username = "manag", roles = "MANAGER")
    void testViewAvailableRoomsForm() throws Exception {
        mockMvc.perform(get("/hotel/view/available/rooms"))
                .andExpect(status().isOk())
                .andExpect(view().name("viewAvailableRooms"));
    }

    @Test
    @WithMockUser(username = "manag", roles = "MANAGER")
    void testViewBookedRoomsForm() throws Exception {
        mockMvc.perform(get("/hotel/view/booked/rooms"))
                .andExpect(status().isOk())
                .andExpect(view().name("viewBookedRooms"));
    }

    @Test
    @WithMockUser(username = "manag", roles = "MANAGER")
    void testBookingCancellationForm() throws Exception {
        mockMvc.perform(get("/hotel/booking/cancellation"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookingCancellation"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testRateHotelForm() throws Exception {
        mockMvc.perform(get("/hotel/rate/hotel"))
                .andExpect(status().isOk())
                .andExpect(view().name("rateHotel"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testBookmarkHotelForm() throws Exception {
        mockMvc.perform(get("/hotel/add/bookmark"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookmarkHotel"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testSeeAllHotelsInBookmarks() throws Exception {
        mockMvc.perform(get("/hotel/see/all/hotels/in/bookmarks"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookmarks"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testGetProfile() throws Exception {
        mockMvc.perform(get("/hotel/see/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("getProfile"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testPasswordResetForm() throws Exception {
        mockMvc.perform(get("/hotel/password/reset"))
                .andExpect(status().isOk())
                .andExpect(view().name("passwordReset"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testTopUpBalanceForm() throws Exception {
        mockMvc.perform(get("/hotel/top/up/balance"))
                .andExpect(status().isOk())
                .andExpect(view().name("topUpBalance"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void testBookRoomForm() throws Exception {
        mockMvc.perform(get("/hotel/book/room"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookRoom"));
    }

}