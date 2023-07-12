package lk.lnas.ims.repos;

import lk.lnas.ims.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public interface DashboardRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    @Query("SELECT SUM(t.amount) FROM Transaction t")
    BigDecimal calculateTotalSales();

    @Query("SELECT SUM(p.qty) FROM Production p")
    BigDecimal calculateTotalProduction();

    @Query("SELECT COUNT(p) FROM Plant p")
    BigDecimal calculateTotalCount();

    @Query("SELECT COUNT(*) FROM Farm")
    BigDecimal calculateTotalFarms();

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE MONTH(t.dateCreated) = MONTH(CURRENT_DATE()) AND YEAR(t.dateCreated) = YEAR(CURRENT_DATE())")
    BigDecimal calculateMonthlySales();

    @Query("SELECT SUM(p.qty) FROM Production p WHERE MONTH(p.dateCreated) = MONTH(CURRENT_DATE()) AND YEAR(p.dateCreated) = YEAR(CURRENT_DATE())")
    BigDecimal calculateMonthlyProduction();

    @Query("SELECT COUNT(p) FROM Plant p WHERE MONTH(p.dateCreated) = MONTH(CURRENT_DATE())")
    BigDecimal calculateMonthlyPlantCount();

    @Query("SELECT COUNT(f) FROM Farm f WHERE f.status = 'active' AND MONTH(f.dateCreated) = MONTH(CURRENT_DATE())")
    BigDecimal calculateActiveFarmsCount();

    @Query("SELECT WEEK(t.dateCreated) as week, SUM(t.amount) as totalSales " +
            "FROM Transaction t " +
            "WHERE t.dateCreated BETWEEN :startDate AND :endDate " +
            "GROUP BY WEEK(t.dateCreated)")
    List<Object[]> calculateWeeklySales(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT WEEK(p.dateCreated) as week, SUM(p.total) as totalPurchase " +
            "FROM Purchase p " +
            "WHERE p.dateCreated BETWEEN :startDate AND :endDate " +
            "GROUP BY WEEK(p.dateCreated)")
    List<Object[]> calculateWeeklyPurchase(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT p.farm.name, SUM(p.qty) as totalProduction " +
            "FROM Production p " +
            "WHERE p.dateCreated BETWEEN :startDate AND :endDate " +
            "GROUP BY p.farm")
    List<Object[]> calculateMonthlyProductionByFarm(OffsetDateTime startDate, OffsetDateTime endDate);

    @Query("SELECT WEEK(p.dateCreated) as week, SUM(p.qty) as totalProduction " +
            "FROM Production p " +
            "WHERE p.dateCreated BETWEEN :startDate AND :endDate " +
            "GROUP BY WEEK(p.dateCreated)")
    List<Object[]> calculateMonthlyProductionByWeek(OffsetDateTime startDate, OffsetDateTime endDate);

    @Query("SELECT WEEK(p.dateCreated) as week, SUM(p.qty) as totalProduction " +
            "FROM Production p " +
            "WHERE p.status = 'AVAILABLE' AND p.dateCreated BETWEEN :startDate AND :endDate " +
            "GROUP BY WEEK(p.dateCreated)")
    List<Object[]> getTotalProductionQuantityByMonth(OffsetDateTime startDate, OffsetDateTime endDate);


}
