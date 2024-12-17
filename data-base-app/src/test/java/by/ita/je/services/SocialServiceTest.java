package by.ita.je.services;

import by.ita.je.models.Social;
import by.ita.je.repositories.SocialRepository;
import by.ita.je.services.utils.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SocialServiceTest extends TestUtils {

    @Mock
    private SocialRepository socialRepository;

    @InjectMocks
    private SocialService socialService;

    @Test
    void testCreate() {
        UUID id = UUID.randomUUID();
        Social social = buildSocial(id,4.5,1);

        when(socialRepository.save(social)).thenReturn(social);

        Social savedSocial = socialService.create(social);

        assertNotNull(savedSocial);
        assertEquals(savedSocial, social);
        Mockito.verify(socialRepository, Mockito.times(1)).save(social);
    }

    @Test
    void testRead() {
        UUID id = UUID.randomUUID();
        Social social = buildSocial(id,4.5,1);

        when(socialRepository.findById(id)).thenReturn(Optional.of(social));

        Social foundSocial= socialService.read(id);

        assertNotNull(foundSocial);
        assertEquals(foundSocial, social);
        Mockito.verify(socialRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void testReadAll() {
        UUID id = UUID.randomUUID();
        Social social = buildSocial(id,4.5,1);

        when(socialRepository.findAll()).thenReturn(List.of(social, social));

        List<Social> socials = socialService.readAll();

        assertNotNull(socials);
        assertEquals(2, socials.size());
        Assertions.assertTrue(socials.contains(social));
        Mockito.verify(socialRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testUpdate() {
        UUID id = UUID.randomUUID();
        Social social = buildSocial(id,4.5,1);

        when(socialRepository.existsById(id)).thenReturn(true);
        when(socialRepository.save(social)).thenReturn(social);

        Social updatedSocial = socialService.update(social);

        assertNotNull(updatedSocial);
        assertEquals(updatedSocial, social);
        Mockito.verify(socialRepository, Mockito.times(1)).existsById(id);
        Mockito.verify(socialRepository, Mockito.times(1)).save(social);
    }

    @Test
    void testDelete() {
        UUID id = UUID.randomUUID();
        Social social = buildSocial(id,4.5,1);

        when(socialRepository.findById(id)).thenReturn(Optional.of(social));

        Social deletedSocial = socialService.delete(id);

        assertNotNull(deletedSocial);
        assertEquals(id, deletedSocial.getId());
        Mockito.verify(socialRepository, Mockito.times(1)).findById(id);
        Mockito.verify(socialRepository, Mockito.times(1)).delete(social);
    }

    @Test
    void deleteAll() {
        socialService.deleteAll();

        Mockito.verify(socialRepository, Mockito.times(1)).deleteAll();
    }
}