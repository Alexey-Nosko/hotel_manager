package by.ita.je.services;

import by.ita.je.models.Hotel;
import by.ita.je.models.Social;
import by.ita.je.repositories.HotelRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    public Hotel create(Hotel hotel) {
        if (hotelRepository.existsByName(hotel.getName())) {
            throw new IllegalArgumentException("Hotel with name " + hotel.getName() + " already exists");
        }

        if (hotel.getRooms() != null) {
            hotel.getRooms().forEach(room -> room.setHotel(hotel));
        }
        if (hotel.getAmenities() != null) {
            hotel.getAmenities().setHotel(hotel);
        }
        if (hotel.getSocial() != null) {
            hotel.getSocial().setHotel(hotel);
        }
        return hotelRepository.save(hotel);
    }

    public Hotel read(UUID id) {
        return hotelRepository.findById(id).orElse(null);
    }

    public List<Hotel> readAll() {
        return hotelRepository.findAll();
    }

    public Hotel update(Hotel hotel) {
        if (hotelRepository.existsById(hotel.getId())) {
            return hotelRepository.save(hotel);
        }
        return null;
    }

    public Hotel delete(UUID id) {
        Hotel foundHotel = hotelRepository.findById(id).orElse(null);

        if (foundHotel != null) {
            hotelRepository.delete(foundHotel);
        }
        return foundHotel;
    }

    public void deleteAll() {
        hotelRepository.deleteAll();
    }

    public List<Hotel> filterHotels(
            Optional<String> name,
            Optional<String> location,
            Optional<Double> minRating,
            Optional<Boolean> wifi,
            Optional<Boolean> pool,
            Optional<Boolean> airConditioner,
            Optional<Boolean> parking
    ) {
        Specification<Hotel> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            name.ifPresent(n ->
                    predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + n.toLowerCase() + "%"
                    ))
            );

            location.ifPresent(l ->
                    predicates.add(criteriaBuilder.equal(root.get("location"), l))
            );

            minRating.ifPresent(rating -> {
                // Явное указание на join с таблицей Social
                Join<Hotel, Social> socialJoin = root.join("social", JoinType.LEFT);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(socialJoin.get("rating"), rating));
            });

            wifi.ifPresent(w -> {
                Join<Object, Object> amenitiesJoin = root.join("amenities", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(amenitiesJoin.get("wifi"), w));
            });

            pool.ifPresent(p -> {
                Join<Object, Object> amenitiesJoin = root.join("amenities", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(amenitiesJoin.get("pool"), p));
            });

            airConditioner.ifPresent(a -> {
                Join<Object, Object> amenitiesJoin = root.join("amenities", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(amenitiesJoin.get("airConditioner"), a));
            });

            parking.ifPresent(p -> {
                Join<Object, Object> amenitiesJoin = root.join("amenities", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(amenitiesJoin.get("parking"), p));
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return hotelRepository.findAll(specification);
    }

    public Hotel findHotelByName(String name) {
        return hotelRepository.findByName(name)
                .orElseThrow(() -> new NoSuchElementException("Hotel with name '" + name + "' not found"));
    }

    @Transactional
    public boolean updateHotelByName(String name, Hotel updatedHotelData) {
        Optional<Hotel> optionalHotel = hotelRepository.findByName(name);

        if (optionalHotel.isEmpty()) {
            System.out.println("Hotel with name " + name + " not found.");
            return false;
        }

        Hotel hotel = optionalHotel.get();

        hotel.setLocation(updatedHotelData.getLocation());
        hotel.setDescription(updatedHotelData.getDescription());
        hotel.setPeriodOfWork(updatedHotelData.getPeriodOfWork());
        hotel.setRooms(updatedHotelData.getRooms());
        hotel.setAmenities(updatedHotelData.getAmenities());
        hotel.setSocial(updatedHotelData.getSocial());

        if (updatedHotelData.getProfiles() != null) {
            hotel.getProfiles().clear();
            hotel.getProfiles().addAll(updatedHotelData.getProfiles());
        }

        if (hotel.getRooms() != null) {
            hotel.getRooms().forEach(room -> room.setHotel(hotel));
        }
        if (hotel.getAmenities() != null) {
            hotel.getAmenities().setHotel(hotel);
        }
        if (hotel.getSocial() != null) {
            hotel.getSocial().setHotel(hotel);
        }
        if (hotel.getProfiles() != null) {
            hotel.getProfiles().forEach(room -> room.setHotel(hotel));
        }

        hotelRepository.save(hotel);
        return true;
    }

    public void deleteHotelByName(String name) {
        hotelRepository.deleteByName(name);
    }
}
