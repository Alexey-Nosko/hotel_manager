package by.ita.je.services;

import by.ita.je.models.Notification;
import by.ita.je.repositories.NotificationRepository;
import by.ita.je.services.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest extends TestUtils {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void testCreate() {
        UUID id = UUID.randomUUID();
        Notification notification = buildNotification(id,"message");

        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification savedNotification = notificationService.create(notification);

        assertNotNull(savedNotification);
        assertEquals(savedNotification, notification);
        Mockito.verify(notificationRepository, Mockito.times(1)).save(notification);
    }

    @Test
    void testRead() {
        UUID id = UUID.randomUUID();
        Notification notification = buildNotification(id,"message");

        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));

        Notification foundNotification = notificationService.read(id);

        assertNotNull(foundNotification);
        assertEquals(foundNotification, notification);
        Mockito.verify(notificationRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void testReadAll() {
        UUID id = UUID.randomUUID();
        Notification notification = buildNotification(id,"message");

        when(notificationRepository.findAll()).thenReturn(List.of(notification, notification));

        List<Notification> notifications = notificationService.readAll();

        assertNotNull(notifications);
        assertEquals(2, notifications.size());
        Assertions.assertTrue(notifications.contains(notification));
        Mockito.verify(notificationRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testUpdate() {
        UUID id = UUID.randomUUID();
        Notification notification = buildNotification(id,"message");

        when(notificationRepository.existsById(id)).thenReturn(true);
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification updatedNotification = notificationService.update(notification);

        assertNotNull(updatedNotification);
        assertEquals(updatedNotification, notification);
        Mockito.verify(notificationRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(notificationRepository, Mockito.times(1)).save(notification);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        Notification notification = buildNotification(id,"message");

        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));

        Notification deletedNotification = notificationService.delete(id);

        assertNotNull(deletedNotification);
        assertEquals(id, deletedNotification.getId());
        Mockito.verify(notificationRepository, Mockito.times(1)).findById(id);
        Mockito.verify(notificationRepository, Mockito.times(1)).delete(notification);
    }

    @Test
    void deleteAll() {
        notificationService.deleteAll();

        Mockito.verify(notificationRepository, Mockito.times(1)).deleteAll();
    }
}