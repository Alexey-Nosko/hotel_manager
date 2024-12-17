package by.ita.je.services;

import by.ita.je.models.Hotel;
import by.ita.je.models.Profile;
import by.ita.je.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public Profile create(Profile profile) {

        return profileRepository.save(profile);
    }

    public Profile read(UUID id) {
        return profileRepository.findById(id).orElse(null);
    }

    public List<Profile> readAll() {
        return profileRepository.findAll();
    }

    public Profile update(Profile profile) {
        if (profileRepository.existsById(profile.getId())) {

            if (profile.getNotifications() != null) {
                profile.getNotifications().forEach(notification -> notification.setProfile(profile));
            }

            return profileRepository.save(profile);
        }
        return null;
    }

    public Profile delete(UUID id) {
        Profile foundUser = profileRepository.findById(id).orElse(null);
        if (foundUser != null) {
            profileRepository.delete(foundUser);
        }
        return foundUser;
    }

    public void deleteAll() {
        profileRepository.deleteAll();
    }

    public Profile hotelManagerRegistration(Hotel hotel) {


        hotel.getProfiles().get(hotel.getProfiles().size() - 1).setHotel(hotel);


        return profileRepository.save(hotel.getProfiles().get(hotel.getProfiles().size() - 1));
    }

    public Optional<Profile> findByLogin(String login) {
        return profileRepository.findByLogin(login);
    }

    public Profile addFavoriteHotel(UUID profileId, UUID hotelId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with id: " + profileId));
        profile.getFavoriteHotels().add(hotelId);
        return profileRepository.save(profile);
    }

    public void passwordReset(String login, String password) {

       Optional<Profile> profile = profileRepository.findByLogin(login);

        if (profile.isPresent()) {
            Profile profile1 = profile.get();

            profile1.setPassword(password);

            if (profile1.getNotifications() != null) {
                profile1.getNotifications().forEach(notification -> notification.setProfile(profile1));
            }

            profileRepository.save(profile1);
        }
    }

    public void topUpBalance(String login, Double balance) {

        Optional<Profile> profile = profileRepository.findByLogin(login);

        if (profile.isPresent()) {
            Profile profile1 = profile.get();

            profile1.setBalance(profile1.getBalance()+balance);

            if (profile1.getNotifications() != null) {
                profile1.getNotifications().forEach(notification -> notification.setProfile(profile1));
            }

            profileRepository.save(profile1);
        }
    }
}