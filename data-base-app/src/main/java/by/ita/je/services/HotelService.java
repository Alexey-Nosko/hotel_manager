package by.ita.je.services;

import by.ita.je.models.Hotel;
import by.ita.je.repositories.HotelRepository;
import lombok.RequiredArgsConstructor;
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

    public List<Hotel> findByName(String name){
        return  hotelRepository.findByName(name);
    }

    public List<Hotel> findByRating(Double rating){
        return  hotelRepository.findByRating(rating);
    }

    public List<Hotel> findByLocation(String location){
        return  hotelRepository.findByLocation(location);
    }

    public List<Hotel> getFilteredHotels(Boolean wifi, Boolean pool, Boolean airConditioner, Boolean parking) {
        return hotelRepository.findByAmenities(wifi, pool, airConditioner, parking);
    }
}
