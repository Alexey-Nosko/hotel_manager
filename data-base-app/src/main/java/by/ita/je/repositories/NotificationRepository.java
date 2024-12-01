package by.ita.je.repositories;

import by.ita.je.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}
