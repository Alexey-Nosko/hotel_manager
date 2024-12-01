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
import java.util.UUID;
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

    @GetMapping("/read/{id}")
    public HotelDto read(@PathVariable UUID id) {
        return hotelMapper.toDto(hotelService.read(id));
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
    public void delete(@PathVariable UUID uuid) {
       hotelService.delete(uuid);
    }

    @DeleteMapping("/delete/all")
    public void deleteAll() {
        hotelService.deleteAll();
    }

    @GetMapping("/read/all/sorted")
    public List<HotelDto> getSortedHotels(@RequestParam(defaultValue = "name") String sortBy) {

        List<Hotel> hotels = hotelService.getSortedHotels(sortBy);

        return hotels.stream()
                .map(hotelMapper::toDto)
                .collect(Collectors.toList());
    }
}