package by.ita.je.repositories;

import by.ita.je.models.Hotel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, UUID>, JpaSpecificationExecutor<Hotel> {
    boolean existsByName(String name);
    Optional<Hotel> findByName(String name);
    @Transactional
    void deleteByName(String name);
}
