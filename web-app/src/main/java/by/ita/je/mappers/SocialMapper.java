package by.ita.je.mappers;

import by.ita.je.dto.SocialDto;
import by.ita.je.models.Social;
import org.springframework.stereotype.Component;

@Component
public class SocialMapper {

    public SocialDto toDto(Social social) {
        if (social == null) {return null;}
        return SocialDto.builder()
                .id(social.getId())
                .rating(social.getRating())
                .followersCount(social.getFollowersCount())
                .build();
    }

    public Social toEntity(SocialDto dto) {
        if (dto == null) {return null;}
        return Social.builder()
                .id(dto.getId())
                .rating(dto.getRating())
                .followersCount(dto.getFollowersCount())
                .build();
    }
}
