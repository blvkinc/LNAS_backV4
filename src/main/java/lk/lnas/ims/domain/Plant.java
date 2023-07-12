package lk.lnas.ims.domain;

import jakarta.persistence.*;
import lk.lnas.ims.model.PlantStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Plant {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String productId;

    @Column(precision = 10, scale = 2)
    private BigDecimal salesPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(nullable = false)
    private Long qtyAtHand;

    @Column(nullable = false)
    private Long qtyPotential;

    @Column
    private String scientificName;

    @Column(name = "\"description\"")
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PlantStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
