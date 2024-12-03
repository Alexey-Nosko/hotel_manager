package by.ita.je.mappers;

import by.ita.je.dto.NotificationDto;
import by.ita.je.models.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationDto toDto(Notification notification) {
        if (notification == null) {return null;}
        return NotificationDto.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .build();
    }

    public Notification toEntity(NotificationDto dto) {
        if (dto == null) {return null;}
        return Notification.builder()
                .id(dto.getId())
                .message(dto.getMessage())
                .build();
    }
}