package by.ita.je.services;

import by.ita.je.models.Client;
import by.ita.je.repositories.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository userRepository;

    public Client create(Client user) {
        return userRepository.save(user);
    }

    public Client read(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<Client> readAll() {
        return userRepository.findAll();
    }

    public Client update(Client user) {
        if (userRepository.existsById(user.getId())) {
            return userRepository.save(user);
        }
        return null;
    }

    public Client delete(UUID id) {
        Client foundUser = userRepository.findById(id).orElse(null);
        if (foundUser != null) {
            userRepository.delete(foundUser);
        }
        return foundUser;
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }
}