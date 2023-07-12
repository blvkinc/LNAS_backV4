package lk.lnas.ims.repos;

import lk.lnas.ims.domain.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
}
