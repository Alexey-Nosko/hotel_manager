package by.ita.je.services;

import by.ita.je.models.Hotel;
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

    public Room bookingCancellation(UUID id, Hotel hotel) {

        Room roomToUpdate = hotel.getRooms().stream()
                .filter(room -> room.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Комната с ID " + id + " не найдена"));

        roomToUpdate.setHotel(hotel);

        return roomRepository.save(roomToUpdate);
    }

    public Room changeHotelRoomConfiguration(UUID id, Hotel hotel) {
        Room roomToUpdate = hotel.getRooms().stream()
                .filter(room -> room.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Комната с ID " + id + " не найдена"));

        roomToUpdate.setHotel(hotel);

        hotel.getRooms().forEach(room -> room.setHotel(hotel));

        return roomRepository.save(roomToUpdate);
    }
}