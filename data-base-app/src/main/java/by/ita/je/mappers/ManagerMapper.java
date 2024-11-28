package by.ita.je.mappers;

import by.ita.je.dto.ManagerDto;
import by.ita.je.models.Manager;
import org.springframework.stereotype.Component;

@Component
public class ManagerMapper {

    public ManagerDto toDto(Manager manager) {
        if (manager == null) return null;
        return ManagerDto.builder()
                .id(manager.getId())
                .name(manager.getName())
                .login(manager.getLogin())
                .password(manager.getPassword())
                .build();
    }

    public Manager toEntity(ManagerDto managerDto) {
        if (managerDto == null) return null;
        return Manager.builder()
                .id(managerDto.getId())
                .name(managerDto.getName())
                .login(managerDto.getLogin())
                .password(managerDto.getPassword())
                .build();
    }
}
