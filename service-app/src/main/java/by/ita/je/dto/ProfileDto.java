package by.ita.je.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {

    private UUID id;

    private String role;

    private String name;

    private String login;

    private String password;

    private Set<UUID> favoriteHotels;

    private List<NotificationDto> notificationsDto;
}
