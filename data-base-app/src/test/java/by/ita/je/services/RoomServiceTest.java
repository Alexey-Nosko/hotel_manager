package by.ita.je.services;

import by.ita.je.models.Room;
import by.ita.je.repositories.RoomRepository;
import by.ita.je.services.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest extends TestUtils {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    @Test
    void testCreate() {
        UUID id = UUID.randomUUID();
        Room room = buildRoom(id,100,"Single",250,true);

        when(roomRepository.save(room)).thenReturn(room);

        Room savedRoom = roomService.create(room);

        assertNotNull(savedRoom);
        assertEquals(savedRoom, room);
        Mockito.verify(roomRepository, Mockito.times(1)).save(room);
    }

    @Test
    void testRead() {
        UUID id = UUID.randomUUID();
        Room room = buildRoom(id,100,"Single",250,true);

        when(roomRepository.findById(id)).thenReturn(Optional.of(room));

        Room foundRoom = roomService.read(id);

        assertNotNull(foundRoom);
        assertEquals(foundRoom, room);
        Mockito.verify(roomRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void testReadAll() {
        UUID id = UUID.randomUUID();
        Room room = buildRoom(id,100,"Single",250,true);

        when(roomRepository.findAll()).thenReturn(List.of(room, room));

        List<Room> rooms = roomService.readAll();

        assertNotNull(rooms);
        assertEquals(2, rooms.size());
        Assertions.assertTrue(rooms.contains(room));
        Mockito.verify(roomRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testUpdate() {
        UUID id = UUID.randomUUID();
        Room room = buildRoom(id,100,"Single",250,true);

        when(roomRepository.existsById(id)).thenReturn(true);
        when(roomRepository.save(room)).thenReturn(room);

        Room updatedRoom = roomService.update(room);

        assertNotNull(updatedRoom);
        assertEquals(updatedRoom, room);
        Mockito.verify(roomRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(roomRepository, Mockito.times(1)).save(room);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        Room room = buildRoom(id,100,"Single",250,true);

        when(roomRepository.findById(id)).thenReturn(Optional.of(room));

        Room deletedRoom = roomService.delete(id);

        assertNotNull(deletedRoom);
        assertEquals(id, deletedRoom.getId());
        Mockito.verify(roomRepository, Mockito.times(1)).findById(id);
        Mockito.verify(roomRepository, Mockito.times(1)).delete(room);
    }

    @Test
    void deleteAll() {
        roomService.deleteAll();

        Mockito.verify(roomRepository, Mockito.times(1)).deleteAll();
    }
}