package by.ita.je.services;

import by.ita.je.models.Notification;
import by.ita.je.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification create(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification read(UUID id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public List<Notification> readAll() {
        return notificationRepository.findAll();
    }

    public Notification update(Notification notification) {
        if (notificationRepository.existsById(notification.getId())) {
            return notificationRepository.save(notification);
        }
        return null;
    }

    public Notification delete(UUID id) {
        Notification foundNotification = notificationRepository.findById(id).orElse(null);
        if (foundNotification != null) {
            notificationRepository.delete(foundNotification);
        }
        return foundNotification;
    }

    public void deleteAll() {
        notificationRepository.deleteAll();
    }
}