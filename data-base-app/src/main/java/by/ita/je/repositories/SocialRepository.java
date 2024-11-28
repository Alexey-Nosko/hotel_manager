package by.ita.je.repositories;

import by.ita.je.models.Social;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SocialRepository extends JpaRepository<Social, UUID> {
}
