package arcee.oficinaback.password_recovery;


import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tb_password_recovery")
public class PasswordRecovery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String recoveryId;

    private String ownerId;

    private String recoveryCode;

    private Boolean isActive = true;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public String getRecoveryId() {
        return recoveryId;
    }

    public void setRecoveryId(String recoveryId) {
        this.recoveryId = recoveryId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getRecoveryCode() {
        return recoveryCode;
    }

    public void setRecoveryCode(String recoveryCode) {
        this.recoveryCode = recoveryCode;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
