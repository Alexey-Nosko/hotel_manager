package by.ita.je.mappers;

import by.ita.je.dto.ProfileDto;
import by.ita.je.models.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Collections;
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
                .balance(profile.getBalance())
                .favoriteHotels(profile.getFavoriteHotels())
                .notificationsDto(profile.getNotifications() != null ?
                        profile.getNotifications().stream()
                                .map(notificationMapper::toDto)
                                .collect(Collectors.toList()) : Collections.emptyList())
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
                .balance(dto.getBalance())
                .favoriteHotels(dto.getFavoriteHotels())
                .notifications(dto.getNotificationsDto() != null ?
                        dto.getNotificationsDto().stream()
                                .map(notificationMapper::toEntity)
                                .collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }
}