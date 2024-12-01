package by.ita.je.services;

import by.ita.je.models.Hotel;
import by.ita.je.repositories.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    public Hotel create(Hotel hotel) {
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

    public List<Hotel> getSortedHotels(String sortBy) {
        Sort sort = Sort.by(Sort.Order.asc("name"));
        if ("name".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Order.asc("name"));
        } else if ("location".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Order.asc("location"));
        } else if ("rating".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Order.desc("social.rating"));
        } else if ("amenities".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Order.desc("amenities.wifi"),
                    Sort.Order.desc("amenities.pool"),
                    Sort.Order.desc("amenities.airConditioner"),
                    Sort.Order.desc("amenities.parking"));
        }
        return hotelRepository.findAll(sort);
    }
}
