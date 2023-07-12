package lk.lnas.ims.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;


@Entity
@Table(name = "\"user\"")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class User {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String mfaSecret;

    @Column(unique = true)
    private String email;

    @Column
    private String phone;

    @Column
    private String address;

    @Column(nullable = false)
    private Boolean isEmailVerified;

    @Column(nullable = false)
    private Boolean isPhoneVerified;

    @Column(nullable = false)
    private Boolean isTotpVerified;

    @Column
    private Boolean isTempPassword;

    @Column
    private Boolean isMfaEnabled;

    @Column
    private Boolean isBanned;

    @Column
    private Boolean isApproved;

    @Column
    @Enumerated(EnumType.STRING)
    private lk.lnas.ims.model.Role role;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @PrePersist
    public void onCreate() {
        dateCreated = lastUpdated = OffsetDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        lastUpdated = OffsetDateTime.now();
    }

}
