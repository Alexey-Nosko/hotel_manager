package by.ita.je.mappers;

import by.ita.je.dto.SocialDto;
import by.ita.je.models.Social;
import org.springframework.stereotype.Component;

@Component
public class SocialMapper {

    public SocialDto toDto(Social social) {
        if (social == null) return null;
        return SocialDto.builder()
                .id(social.getId())
                .followersCount(social.getFollowersCount())
                .build();
    }

    public Social toEntity(SocialDto socialDto) {
        if (socialDto == null) return null;
        return Social.builder()
                .id(socialDto.getId())
                .followersCount(socialDto.getFollowersCount())
                .build();
    }
}
