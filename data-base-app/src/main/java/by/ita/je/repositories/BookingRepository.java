package by.ita.je.repositories;

import by.ita.je.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId AND (b.checkInDate <= :endDate AND b.checkOutDate >= :startDate)")
    List<Booking> findByIdAndDateRange(@Param("roomId") UUID roomId,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);
}
