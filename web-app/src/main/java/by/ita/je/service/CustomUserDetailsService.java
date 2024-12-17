package by.ita.je.service;

import by.ita.je.dto.ProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String url = "/find?login=" + username;
        ResponseEntity<ProfileDto> response = restTemplate.getForEntity(url, ProfileDto.class);

        if (response.getBody() == null) {
            throw new UsernameNotFoundException("User not found");
        }

        ProfileDto profile = response.getBody();
        return User.builder()
                .username(profile.getLogin())
                .password(profile.getPassword())
                .roles(profile.getRole())
                .build();
    }
}
