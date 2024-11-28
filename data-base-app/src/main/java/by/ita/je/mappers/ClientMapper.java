package by.ita.je.mappers;

import by.ita.je.dto.ClientDto;
import by.ita.je.models.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientDto toDto(Client user) {
        if (user == null) return null;
        return ClientDto.builder()
                .id(user.getId())
                .name(user.getName())
                .login(user.getLogin())
                .password(user.getPassword())
                .build();
    }

    public Client toEntity(ClientDto userDto) {
        if (userDto == null) return null;
        return Client.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .login(userDto.getLogin())
                .password(userDto.getPassword())
                .build();
    }
}