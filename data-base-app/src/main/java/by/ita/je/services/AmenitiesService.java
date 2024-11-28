package by.ita.je.services;

import by.ita.je.models.Amenities;
import by.ita.je.repositories.AmenitiesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AmenitiesService {

    private final AmenitiesRepository amenitiesRepository;

    public Amenities create(Amenities amenities) {
        return amenitiesRepository.save(amenities);
    }

    public Amenities read(UUID id) {
        return amenitiesRepository.findById(id).orElse(null);
    }

    public List<Amenities> readAll() {
        return amenitiesRepository.findAll();
    }

    public Amenities update(Amenities amenities) {
        if (amenitiesRepository.existsById(amenities.getId())) {
            return amenitiesRepository.save(amenities);
        }
        return null;
    }

    public Amenities delete(UUID id) {
        Amenities foundAmenities = amenitiesRepository.findById(id).orElse(null);
        if (foundAmenities != null) {
            amenitiesRepository.delete(foundAmenities);
        }
        return foundAmenities;
    }

    public void deleteAll() {
        amenitiesRepository.deleteAll();
    }
}