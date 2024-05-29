package arcee.oficinaback.works;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkRepository extends JpaRepository<work_entity, UUID> {

    List<work_entity> findAllByOwnerId(String ownerId);

    Optional<work_entity>findWorkByNameAndOwnerId(String name, String ownerId);

    Optional<work_entity>findWorkByIdAndOwnerId(String id, String ownerId);
}
