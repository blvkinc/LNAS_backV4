package lk.lnas.ims.repos;

import lk.lnas.ims.domain.Production;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;


public interface ProductionRepository extends JpaRepository<Production, Long>, JpaSpecificationExecutor<Production> {
    @Query("SELECT DATE_FORMAT(p.dateCreated, '%Y-%m-01'), SUM(p.qty) " +
            "FROM Production p " +
            "WHERE p.dateCreated BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE_FORMAT(p.dateCreated, '%Y-%m-01')")
    List<Object[]> findMonthlyProductionQuantities(@Param("startDate") OffsetDateTime startDate,
                                                   @Param("endDate") OffsetDateTime endDate);

    @Query("SELECT DATE_FORMAT(p.dateCreated, '%Y-%m-%d'), SUM(p.qty) " +
            "FROM Production p " +
            "WHERE p.dateCreated BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE_FORMAT(p.dateCreated, '%Y-%m-%d')")
    List<Object[]> findWeeklyProductionQuantities(@Param("startDate") OffsetDateTime startDate,
                                                  @Param("endDate") OffsetDateTime endDate);

    @Query("SELECT p.status, COUNT(p) " +
            "FROM Production p " +
            "GROUP BY p.status")
    List<Object[]> findProductionSummaryByStatus();

    @Query("SELECT DATE_FORMAT(p.dateCreated, '%Y-%m'), p.farm, SUM(p.qty) " +
            "FROM Production p " +
            "WHERE p.dateCreated BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE_FORMAT(p.dateCreated, '%Y-%m'), p.farm")
    List<Object[]> findMonthlyProductionByFarm(@Param("startDate") OffsetDateTime startDate,
                                               @Param("endDate") OffsetDateTime endDate);
}
