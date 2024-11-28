package by.ita.je.services;

import by.ita.je.models.Room;
import by.ita.je.repositories.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room create(Room room) {
        return roomRepository.save(room);
    }

    public Room read(UUID id) {
        return roomRepository.findById(id).orElse(null);
    }

    public List<Room> readAll() {
        return roomRepository.findAll();
    }

    public Room update(Room room) {
        if (roomRepository.existsById(room.getId())) {
            return roomRepository.save(room);
        }
        return null;
    }

    public Room delete(UUID id) {
        Room foundRoom = roomRepository.findById(id).orElse(null);
        if (foundRoom != null) {
            roomRepository.delete(foundRoom);
        }
        return foundRoom;
    }

    public void deleteAll() {
        roomRepository.deleteAll();
    }
}