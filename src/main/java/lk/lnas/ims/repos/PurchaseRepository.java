package lk.lnas.ims.repos;

import lk.lnas.ims.domain.Purchase;
import lk.lnas.ims.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface PurchaseRepository extends JpaRepository<Purchase, Long>, JpaSpecificationExecutor<Purchase> {
    List<Purchase> findByDateCreatedBetween(LocalDateTime startDate, LocalDateTime endDate);

    Optional<Purchase> findFirstByOrderByDocumentIndexDesc();
}
