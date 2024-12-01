package by.ita.je.controllers;

import by.ita.je.dto.RoomDto;
import by.ita.je.mappers.RoomMapper;
import by.ita.je.models.Room;
import by.ita.je.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(path = "/database/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @PostMapping("/create")
    public void create(@RequestBody RoomDto roomDto){
        Room room = roomMapper.toEntity(roomDto);
        roomService.create(room);
    }

    @GetMapping("/read/{id}")
    public RoomDto read(@PathVariable UUID id) {
        return roomMapper.toDto(roomService.read(id));
    }

    @GetMapping("/read/all")
    public List<RoomDto> readALL()
    {
        List<Room> rooms = roomService.readAll();
        List<RoomDto> roomDtos = rooms.stream().map(roomMapper::toDto).toList();
        return roomDtos;
    }

    @PutMapping("/update/{uuid}")
    public RoomDto update(@RequestBody RoomDto roomDto){

        Room room = roomMapper.toEntity(roomDto);

        return roomMapper.toDto(roomService.update(room));
    }

    @DeleteMapping("/delete/{uuid}")
    public void delete(@PathVariable UUID uuid) {
        roomService.delete(uuid);
    }

    @DeleteMapping("/delete/all")
    public void deleteAll() {
        roomService.deleteAll();
    }
}