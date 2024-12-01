package by.ita.je.controllers;

import by.ita.je.dto.ProfileDto;
import by.ita.je.mappers.ProfileMapper;
import by.ita.je.models.Profile;
import by.ita.je.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(path = "/database/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    @PostMapping("/create")
    public void create(@RequestBody ProfileDto profileDto){
        Profile profile = profileMapper.toEntity(profileDto);
        profileService.create(profile);
    }

    @GetMapping("/read/{id}")
    public ProfileDto read(@PathVariable UUID id) {
        return profileMapper.toDto(profileService.read(id));
    }

    @GetMapping("/read/all")
    public List<ProfileDto> readALL()
    {
        List<Profile> profiles = profileService.readAll();
        List<ProfileDto> profileDtos = profiles.stream().map(profileMapper::toDto).toList();
        return profileDtos;
    }

    @PutMapping("/update/{uuid}")
    public ProfileDto update(@RequestBody ProfileDto profileDto){

        Profile profile = profileMapper.toEntity(profileDto);

        return profileMapper.toDto(profileService.update(profile));
    }

    @DeleteMapping("/delete/{uuid}")
    public void delete(@PathVariable UUID uuid) {
        profileService.delete(uuid);
    }

    @DeleteMapping("/delete/all")
    public void deleteAll() {
        profileService.deleteAll();
    }
}