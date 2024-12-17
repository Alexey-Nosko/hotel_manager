package by.ita.je.services;

import by.ita.je.models.Profile;
import by.ita.je.repositories.ProfileRepository;
import by.ita.je.services.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest extends TestUtils {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void testCreate() {
        UUID id = UUID.randomUUID();
        Profile profile = buildProfile(id,"client","Mark","login","password",200.0);

        when(profileRepository.save(profile)).thenReturn(profile);

        Profile savedProfile = profileService.create(profile);

        assertNotNull(savedProfile);
        assertEquals(savedProfile, profile);
        Mockito.verify(profileRepository, Mockito.times(1)).save(profile);
    }

    @Test
    void testRead() {
        UUID id = UUID.randomUUID();
        Profile profile = buildProfile(id,"client","Mark","login","password",200.0);

        when(profileRepository.findById(id)).thenReturn(Optional.of(profile));

        Profile foundProfile = profileService.read(id);

        assertNotNull(foundProfile);
        assertEquals(foundProfile, profile);
        Mockito.verify(profileRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void testReadAll() {
        UUID id = UUID.randomUUID();
        Profile profile = buildProfile(id,"client","Mark","login","password",200.0);

        when(profileRepository.findAll()).thenReturn(List.of(profile, profile));

        List<Profile> profiles = profileService.readAll();

        assertNotNull(profiles);
        assertEquals(2, profiles.size());
        Assertions.assertTrue(profiles.contains(profile));
        Mockito.verify(profileRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testUpdate() {
        UUID id = UUID.randomUUID();
        Profile profile = buildProfile(id,"client","Mark","login","password",200.0);

        when(profileRepository.existsById(id)).thenReturn(true);
        when(profileRepository.save(profile)).thenReturn(profile);

        Profile updatedProfile = profileService.update(profile);

        assertNotNull(updatedProfile);
        assertEquals(updatedProfile, profile);
        Mockito.verify(profileRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(profileRepository, Mockito.times(1)).save(profile);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        Profile profile = buildProfile(id,"client","Mark","login","password",200.0);

        when(profileRepository.findById(id)).thenReturn(Optional.of(profile));

        Profile deletedProfile = profileService.delete(id);

        assertNotNull(deletedProfile);
        assertEquals(id, deletedProfile.getId());
        Mockito.verify(profileRepository, Mockito.times(1)).findById(id);
        Mockito.verify(profileRepository, Mockito.times(1)).delete(profile);
    }

    @Test
    void deleteAll() {
        profileService.deleteAll();

        Mockito.verify(profileRepository, Mockito.times(1)).deleteAll();
    }

    @Test
    void hotelManagerRegistration_HappyPath() {
        UUID id = UUID.randomUUID();
        Profile profile = buildProfile(id,"client","Mark","login","password",200.0);

        when(profileRepository.save(profile)).thenReturn(profile);

        profileRepository.save(profile);

        assertEquals(profile, profile);
        Mockito.verify(profileRepository, Mockito.times(1)).save(profile);
    }

    @Test
    void findByLogin() {

        String login = "testLogin";
        Profile profile = new Profile();
        when(profileRepository.findByLogin(login)).thenReturn(Optional.of(profile));

        Optional<Profile> result = profileService.findByLogin(login);

        assertTrue(result.isPresent());
        assertEquals(profile, result.get());
        Mockito.verify(profileRepository, Mockito.times(1)).findByLogin(login);
    }

    @Test
    void addFavoriteHotel() {
        UUID id = UUID.randomUUID();
        UUID hotelId = UUID.randomUUID();
        Profile profile = buildProfile(id,"client","Mark","login","password",200.0);
        profile.setFavoriteHotels(new HashSet<>());
        profile.getFavoriteHotels().add(UUID.fromString("f6d00b2c-8ab6-49e6-910d-c55fa7d1f9a6"));
        when(profileRepository.findById(id)).thenReturn(Optional.of(profile));
        when(profileRepository.save(profile)).thenReturn(profile);

        Profile result = profileService.addFavoriteHotel(id, hotelId);

        assertEquals(profile, result);
        Mockito.verify(profileRepository,Mockito. times(1)).save(profile);
        Mockito.verify(profileRepository,Mockito. times(1)).findById(id);
    }

    @Test
    void passwordReset() {

        UUID id = UUID.randomUUID();
        Profile profile = buildProfile(id,"client","Mark","login","password",200.0);
        String newPassword = "newPassword123";

        when(profileRepository.findByLogin(profile.getLogin())).thenReturn(Optional.of(profile));

        profileService.passwordReset(profile.getLogin(), newPassword);

        assertEquals(newPassword, profile.getPassword());
        Mockito.verify(profileRepository, Mockito.times(1)).save(profile);
    }

    @Test
    void topUpBalance() {

        UUID id = UUID.randomUUID();
        Profile profile = buildProfile(id,"client","Mark","login","password",200.0);

        when(profileRepository.findByLogin(profile.getLogin())).thenReturn(Optional.of(profile));

        profileService.topUpBalance(profile.getLogin(), 200.0);

        assertEquals(400.0, profile.getBalance());
        Mockito.verify(profileRepository, Mockito.times(1)).save(profile);
    }

}