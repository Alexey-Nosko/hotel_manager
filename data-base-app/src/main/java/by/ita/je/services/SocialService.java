package by.ita.je.services;

import by.ita.je.models.Social;
import by.ita.je.repositories.SocialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SocialService {

    private final SocialRepository socialRepository;

    public Social create(Social social) {
        return socialRepository.save(social);
    }

    public Social read(UUID id) {
        return socialRepository.findById(id).orElse(null);
    }

    public List<Social> readAll() {
        return socialRepository.findAll();
    }

    public Social update(Social social) {
        if (socialRepository.existsById(social.getId())) {
            return socialRepository.save(social);
        }
        return null;
    }

    public Social delete(UUID id) {
        Social foundSocial = socialRepository.findById(id).orElse(null);
        if (foundSocial != null) {
            socialRepository.delete(foundSocial);
        }
        return foundSocial;
    }

    public void deleteAll() {
        socialRepository.deleteAll();
    }
}