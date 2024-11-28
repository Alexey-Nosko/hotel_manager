package by.ita.je.repositories;

import by.ita.je.models.Amenities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AmenitiesRepository extends JpaRepository<Amenities, UUID> {

}
