package by.ita.je.services;

import by.ita.je.models.Profile;
import by.ita.je.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
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
}