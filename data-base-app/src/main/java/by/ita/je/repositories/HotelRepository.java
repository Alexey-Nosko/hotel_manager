package by.ita.je.repositories;

import by.ita.je.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, UUID> {

    List<Hotel> findByName(String name);

    List<Hotel> findByRating(Double rating);

    List<Hotel> findByLocation(String location);

    @Query("SELECT h FROM Hotel h JOIN h.amenities a " +
            "WHERE (:wifi IS NULL OR a.wifi = :wifi) " +
            "AND (:pool IS NULL OR a.pool = :pool) " +
            "AND (:airConditioner IS NULL OR a.airConditioner = :airConditioner) " +
            "AND (:parking IS NULL OR a.parking = :parking)")
    List<Hotel> findByAmenities(
            @Param("wifi") Boolean wifi,
            @Param("pool") Boolean pool,
            @Param("airConditioner") Boolean airConditioner,
            @Param("parking") Boolean parking
    );
}
