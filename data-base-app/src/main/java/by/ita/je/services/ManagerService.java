package by.ita.je.services;

import by.ita.je.models.Manager;
import by.ita.je.repositories.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;

    public Manager create(Manager manager) {
        return managerRepository.save(manager);
    }

    public Manager read(UUID id) {
        return managerRepository.findById(id).orElse(null);
    }

    public List<Manager> readAll() {
        return managerRepository.findAll();
    }

    public Manager update(Manager manager) {
        if (managerRepository.existsById(manager.getId())) {
            return managerRepository.save(manager);
        }
        return null;
    }

    public Manager delete(UUID id) {
        Manager foundManager = managerRepository.findById(id).orElse(null);
        if (foundManager != null) {
            managerRepository.delete(foundManager);
        }
        return foundManager;
    }

    public void deleteAll() {
        managerRepository.deleteAll();
    }
}