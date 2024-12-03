package by.ita.je.mappers;

import by.ita.je.dto.ProfileDto;
import by.ita.je.models.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProfileMapper {

    private final NotificationMapper notificationMapper;

    public ProfileDto toDto(Profile profile) {
        if (profile == null) {return null;}
        return ProfileDto.builder()
                .id(profile.getId())
                .role(profile.getRole())
                .name(profile.getName())
                .login(profile.getLogin())
                .password(profile.getPassword())
                .favoriteHotels(profile.getFavoriteHotels())
                .notificationsDto(profile.getNotifications().stream()
                        .map(notificationMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public Profile toEntity(ProfileDto dto) {
        if (dto == null) {return null;}
        return Profile.builder()
                .id(dto.getId())
                .role(dto.getRole())
                .name(dto.getName())
                .login(dto.getLogin())
                .password(dto.getPassword())
                .favoriteHotels(dto.getFavoriteHotels())
                .notifications(dto.getNotificationsDto().stream()
                        .map(notificationMapper::toEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}