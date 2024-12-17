package by.ita.je.services;

import by.ita.je.models.Amenities;
import by.ita.je.repositories.AmenitiesRepository;
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
class AmenitiesServiceTest extends TestUtils {

    @Mock
    private AmenitiesRepository amenitiesRepository;

    @InjectMocks
    private AmenitiesService amenitiesService;

    @Test
    void testCreate() {
        UUID id = UUID.randomUUID();
        Amenities amenities = buildAmenities(id, true, true, true, true);

        when(amenitiesRepository.save(amenities)).thenReturn(amenities);

        Amenities savedAmenities = amenitiesService.create(amenities);

        assertNotNull(savedAmenities);
        assertEquals(savedAmenities, amenities);
        Mockito.verify(amenitiesRepository, Mockito.times(1)).save(amenities);
    }

    @Test
    void testRead() {
        UUID id = UUID.randomUUID();
        Amenities amenities = buildAmenities(id, true, true, true, true);

        when(amenitiesRepository.findById(id)).thenReturn(Optional.of(amenities));

        Amenities foundAmenities = amenitiesService.read(id);

        assertNotNull(foundAmenities);
        assertEquals(foundAmenities, amenities);
        Mockito.verify(amenitiesRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void testReadAll() {
        UUID id = UUID.randomUUID();
        Amenities amenities = buildAmenities(id, true, true, true, true);

        when(amenitiesRepository.findAll()).thenReturn(List.of(amenities, amenities));

        List<Amenities> amenitiesList = amenitiesService.readAll();

        assertNotNull(amenitiesList);
        assertEquals(2, amenitiesList.size());
        Assertions.assertTrue(amenitiesList.contains(amenities));
        Mockito.verify(amenitiesRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testUpdate() {
        UUID id = UUID.randomUUID();
        Amenities amenities = buildAmenities(id, true, true, true, true);

        when(amenitiesRepository.existsById(id)).thenReturn(true);
        when(amenitiesRepository.save(amenities)).thenReturn(amenities);

        Amenities updatedAmenities = amenitiesService.update(amenities);

        assertNotNull(updatedAmenities);
        assertEquals(updatedAmenities, amenities);
        Mockito.verify(amenitiesRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(amenitiesRepository, Mockito.times(1)).save(amenities);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        Amenities amenities = buildAmenities(id, true, true, true, true);

        when(amenitiesRepository.findById(id)).thenReturn(Optional.of(amenities));

        Amenities deletedAmenities = amenitiesService.delete(id);

        assertNotNull(deletedAmenities);
        assertEquals(id, deletedAmenities.getId());
        Mockito.verify(amenitiesRepository, Mockito.times(1)).findById(id);
        Mockito.verify(amenitiesRepository, Mockito.times(1)).delete(amenities);
    }

    @Test
    void deleteAll() {
        amenitiesService.deleteAll();

        Mockito.verify(amenitiesRepository, Mockito.times(1)).deleteAll();
    }
}
