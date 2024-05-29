package arcee.oficinaback.password_recovery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, UUID> {

    Optional<PasswordRecovery> findByRecoveryId(String recoveryId);

    Optional<PasswordRecovery> findRecoveryByOwnerIdAndIsActive(String ownerId, Boolean isActive);

    Optional<PasswordRecovery> findRecoveryByRecoveryCodeAndIsActive(String code, Boolean isActive);
}
