package by.ita.je.services;

import by.ita.je.models.Hotel;
import by.ita.je.models.Profile;
import by.ita.je.services.utils.TestUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProfileServiceIT extends TestUtils {

    @Autowired
    private ProfileService profileService;

    @Test
    public void findByLogin() {

        Optional<Profile> profileOptional = profileService.findByLogin("al");

        assertTrue(profileOptional.isPresent());
        assertEquals("al", profileOptional.get().getLogin());
    }

    @Test
    @Transactional
    public void addFavoriteHotel() {

        Profile updatedProfile = profileService.
                addFavoriteHotel(UUID.fromString("d9d2e0a5-8cf4-44bc-8a35-c11abf5a50de"),
                        UUID.fromString("e8a3c5b7-7c1e-4f9a-ae8b-9c7f4e8d6a7b"));

         }

    @Test
    @Transactional
    public void passwordReset() {

        profileService.passwordReset("al", "1234");

    }

    @Test
    @Transactional
    public void topUpBalance() {

        profileService.topUpBalance("al", 100.0);

    }

}