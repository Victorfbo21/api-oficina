package arcee.oficinaback.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<users_entity, UUID> {
    List<users_entity> findByIsDeletedFalse();

    Optional<users_entity> findByEmail(String email);

    Optional<users_entity> findById(String id);
}
