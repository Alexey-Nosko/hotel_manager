package by.ita.je.controllers;

import by.ita.je.dto.NotificationDto;
import by.ita.je.mappers.NotificationMapper;
import by.ita.je.models.Notification;
import by.ita.je.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(path = "/database/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;


    @PostMapping("/create")
    public void create(@RequestBody NotificationDto notificationDto){
        Notification notification = notificationMapper.toEntity(notificationDto);
        notificationService.create(notification);
    }

    @GetMapping("/read/{id}")
    public NotificationDto read(@PathVariable UUID id) {
        return notificationMapper.toDto(notificationService.read(id));
    }

    @GetMapping("/read/all")
    public List<NotificationDto> readALL()
    {
        List<Notification> notifications = notificationService.readAll();
        List<NotificationDto> notificationDtos = notifications.stream().map(notificationMapper::toDto).toList();
        return notificationDtos;
    }

    @PutMapping("/update/{uuid}")
    public NotificationDto update(@RequestBody NotificationDto notificationDto){

        Notification notification = notificationMapper.toEntity(notificationDto);

        return notificationMapper.toDto(notificationService.update(notification));
    }

    @DeleteMapping("/delete/{uuid}")
    public void delete(@PathVariable UUID uuid) {
        notificationService.delete(uuid);
    }

    @DeleteMapping("/delete/all")
    public void deleteAll() {
        notificationService.deleteAll();
    }
}