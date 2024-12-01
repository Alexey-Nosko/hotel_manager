package by.ita.je.controllers;

import by.ita.je.dto.AmenitiesDto;
import by.ita.je.mappers.AmenitiesMapper;
import by.ita.je.models.Amenities;
import by.ita.je.services.AmenitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(path = "/database/amenities")
@RequiredArgsConstructor
public class AmenitiesController {

    private final AmenitiesService amenitiesService;
    private final AmenitiesMapper amenitiesMapper;

    @PostMapping("/create")
    public void create(@RequestBody AmenitiesDto amenitiesDto){
        Amenities amenities = amenitiesMapper.toEntity(amenitiesDto);
        amenitiesService.create(amenities);
    }

    @GetMapping("/read/{id}")
    public AmenitiesDto read(@PathVariable UUID id) {
        return amenitiesMapper.toDto(amenitiesService.read(id));
    }

    @GetMapping("/read/all")
    public List<AmenitiesDto> readALL()
    {
        List<Amenities> amenities = amenitiesService.readAll();
        List<AmenitiesDto> amenitiesDtos = amenities.stream().map(amenitiesMapper::toDto).toList();
        return amenitiesDtos;
    }

    @PutMapping("/update/{uuid}")
    public AmenitiesDto update(@RequestBody AmenitiesDto amenitiesDto){

        Amenities amenities = amenitiesMapper.toEntity(amenitiesDto);

        return amenitiesMapper.toDto(amenitiesService.update(amenities));
    }

    @DeleteMapping("/delete/{uuid}")
    public void delete(@PathVariable UUID uuid) {
        amenitiesService.delete(uuid);
    }

    @DeleteMapping("/delete/all")
    public void deleteAll() {
        amenitiesService.deleteAll();
    }
}