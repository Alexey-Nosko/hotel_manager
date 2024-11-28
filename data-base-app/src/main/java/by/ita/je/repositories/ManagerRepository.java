package by.ita.je.repositories;

import by.ita.je.models.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ManagerRepository extends JpaRepository<Manager, UUID> {
}
