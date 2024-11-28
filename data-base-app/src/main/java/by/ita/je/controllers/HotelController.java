package by.ita.je.controllers;

import by.ita.je.dto.HotelDto;
import by.ita.je.mappers.AmenitiesMapper;
import by.ita.je.mappers.HotelMapper;
import by.ita.je.mappers.RoomMapper;
import by.ita.je.models.Hotel;
import by.ita.je.services.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping(path = "/database/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final HotelMapper hotelMapper;
    private final RoomMapper roomMapper;
    private final AmenitiesMapper amenitiesMapper;

    @PostMapping("/create")
    public void create(@RequestBody HotelDto hotelDto){
        Hotel hotel = hotelMapper.toEntity(hotelDto);
        hotelService.create(hotel);
    }

    @GetMapping("/read/{uuid}")
    public HotelDto read(Hotel hotel) {
        return hotelMapper.toDto(hotelService.read(hotel.getId()));
    }

    @GetMapping("/read/all")
    public List<HotelDto> readALL()
    {
        List<Hotel> hotels = hotelService.readAll();
        List<HotelDto> hotelDtos = hotels.stream().map(hotelMapper::toDto).toList();
        return hotelDtos;
    }

    @PutMapping("/update/{uuid}")
    public HotelDto update(@RequestBody HotelDto hotelDto){

        Hotel hotel = hotelMapper.toEntity(hotelDto);

        hotel.setName(hotelDto.getName());
        hotel.setRating(hotelDto.getRating());
        hotel.setLocation(hotelDto.getLocation());
        hotel.setDescription(hotelDto.getDescription());
        hotel.setPeriodOfWork(hotelDto.getPeriodOfWork());
        hotel.setRooms(hotelDto.getRoomsDto() != null
                ? hotelDto.getRoomsDto().stream()
                .map(roomMapper::toEntity)
                .collect(Collectors.toList())
                : null);
        hotel.setAmenities(amenitiesMapper.toEntity(hotelDto.getAmenitiesDto()));

        return hotelMapper.toDto(hotelService.update(hotel));
    }

    @DeleteMapping("/delete/{uuid}")
    public HotelDto delete(HotelDto hotelDto){

        Hotel hotel = hotelMapper.toEntity(hotelDto);
        Hotel deletedHotel = hotelService.delete(hotel.getId());
        HotelDto deletedHotelDto = hotelMapper.toDto(deletedHotel);
        return deletedHotelDto;
    }

    @DeleteMapping("/delete/all")
    public void deleteAll() {
        hotelService.deleteAll();
    }

}