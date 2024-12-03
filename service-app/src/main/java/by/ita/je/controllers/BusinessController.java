package by.ita.je.controllers;

import by.ita.je.dto.HotelDto;
import by.ita.je.mappers.HotelMapper;
import by.ita.je.services.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;
    private final HotelMapper hotelMapper;

    @GetMapping("/get/hotel/list/by/filter")
    public List<HotelDto> getHotelListByFilter(@RequestParam String sortBy) {

        return businessService.getHotelListByFilter(sortBy).stream()
                .map(hotelMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/hotel/create")
    public HotelDto createLaptop(@RequestBody HotelDto hotelDto){

        return hotelMapper.toDto(businessService.createHotel(hotelMapper.toEntity(hotelDto)));
    }

    @GetMapping("/hotel/read/{uuid}")
    public HotelDto readHotel(@PathVariable UUID uuid) {

        return hotelMapper.toDto(businessService.readHotel(uuid));
    }

    @PostMapping("/hotel/update/{uuid}")
    public HotelDto updateLaptop(@PathVariable UUID uuid, @RequestBody HotelDto hotelDto){

        return hotelMapper.toDto(businessService.updateHotel(uuid,hotelMapper.toEntity(hotelDto)));
    }

    @DeleteMapping("/hotel/delete/{uuid}")
    public void delete(@PathVariable UUID uuid)
    {
        businessService.deleteHotel(uuid);
    }


}
