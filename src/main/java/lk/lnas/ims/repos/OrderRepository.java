package lk.lnas.ims.repos;

import lk.lnas.ims.domain.Order;
import lk.lnas.ims.domain.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    @Query("SELECT DATE_FORMAT(o.dateCreated, '%Y-%m-%d'), SUM(o.total) " +
            "FROM Order o " +
            "WHERE o.dateCreated BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE_FORMAT(o.dateCreated, '%Y-%m-%d')")
    List<Object[]> findWeeklyOrderTotals(@Param("startDate") OffsetDateTime startDate,
                                         @Param("endDate") OffsetDateTime endDate);

    @Query("SELECT DATE_FORMAT(o.dateCreated, '%Y-%m'), SUM(o.total) " +
            "FROM Order o " +
            "WHERE o.dateCreated BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE_FORMAT(o.dateCreated, '%Y-%m')")
    List<Object[]> findMonthlyOrderTotals(@Param("startDate") OffsetDateTime startDate,
                                          @Param("endDate") OffsetDateTime endDate);

    Optional<Purchase> findFirstByOrderByDocumentIndexDesc();
}
