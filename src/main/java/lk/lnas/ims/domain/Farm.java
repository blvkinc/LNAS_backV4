package lk.lnas.ims.domain;

import jakarta.persistence.*;
import lk.lnas.ims.model.FarmStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.Set;


@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Farm {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(name = "\"description\"")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FarmStatus status;

    @ManyToMany(mappedBy = "farms", fetch = FetchType.LAZY)
    private Set<Employee> employees;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
