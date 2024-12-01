package by.ita.je.controllers;

import by.ita.je.dto.SocialDto;
import by.ita.je.mappers.SocialMapper;
import by.ita.je.models.Social;
import by.ita.je.services.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(path = "/database/social")
@RequiredArgsConstructor
public class SocialController {

    private final SocialService socialService;
    private final SocialMapper socialMapper;

    @PostMapping("/create")
    public void create(@RequestBody SocialDto socialDto){
        Social social = socialMapper.toEntity(socialDto);
        socialService.create(social);
    }

    @GetMapping("/read/{id}")
    public SocialDto read(@PathVariable UUID id) {
        return socialMapper.toDto(socialService.read(id));
    }

    @GetMapping("/read/all")
    public List<SocialDto> readALL()
    {
        List<Social> socials = socialService.readAll();
        List<SocialDto> socialDtos = socials.stream().map(socialMapper::toDto).toList();
        return socialDtos;
    }

    @PutMapping("/update/{uuid}")
    public SocialDto update(@RequestBody SocialDto socialDto){

        Social social = socialMapper.toEntity(socialDto);

        return socialMapper.toDto(socialService.update(social));
    }

    @DeleteMapping("/delete/{uuid}")
    public void delete(@PathVariable UUID uuid) {
        socialService.delete(uuid);
    }

    @DeleteMapping("/delete/all")
    public void deleteAll() {
        socialService.deleteAll();
    }
}