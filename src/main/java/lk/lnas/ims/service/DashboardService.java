package lk.lnas.ims.service;

import lk.lnas.ims.domain.Purchase;
import lk.lnas.ims.domain.Transaction;
import lk.lnas.ims.repos.DashboardRepository;
import lk.lnas.ims.repos.PurchaseRepository;
import lk.lnas.ims.repos.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardRepository dashboardRepository;
    private final TransactionRepository transactionRepository;
    private final PurchaseRepository purchaseRepository;

    public BigDecimal getTotalSales() {
        return dashboardRepository.calculateTotalSales();
    }

    public BigDecimal getTotalProduction() {
        return dashboardRepository.calculateTotalProduction();
    }

    public BigDecimal getTotalCount() {
        return dashboardRepository.calculateTotalCount();
    }

    public BigDecimal getTotalFarms() {
        return dashboardRepository.calculateTotalFarms();
    }

    public BigDecimal getMonthlySales() {
        return dashboardRepository.calculateMonthlySales();
    }

    public BigDecimal getMonthlyProduction() {
        return dashboardRepository.calculateMonthlyProduction();
    }

    public BigDecimal getMonthlyPlantCount() {
        return dashboardRepository.calculateMonthlyPlantCount();
    }

    public BigDecimal getActiveFarmsCount() {
        return dashboardRepository.calculateActiveFarmsCount();
    }

    public Map<LocalDateTime, BigDecimal> getWeeklySales() {
        LocalDateTime currentDate = LocalDateTime.now();

        LocalDateTime firstDayOfCurrentWeek = getFirstDayOfCurrentWeek(currentDate);

        LocalDateTime lastDayOfCurrentWeek = getLastDayOfCurrentWeek(currentDate);
        lastDayOfCurrentWeek = lastDayOfCurrentWeek.plusDays(1);

        List<Transaction> transactions = transactionRepository.findByDateBetween(firstDayOfCurrentWeek, lastDayOfCurrentWeek);
        Map<LocalDateTime, BigDecimal> dailySalesSummary = new HashMap<>();

        for (Transaction transaction : transactions) {
            LocalDateTime transactionDate = transaction.getDate();
            BigDecimal salesAmount = transaction.getAmount();

            dailySalesSummary.put(transactionDate, dailySalesSummary.getOrDefault(transactionDate, BigDecimal.ZERO).add(salesAmount));
        }

        return dailySalesSummary;
    }

    public Map<LocalDateTime, BigDecimal> getWeeklyPurchase() {
        LocalDateTime currentDate = LocalDateTime.now();

        LocalDateTime firstDayOfCurrentWeek = getFirstDayOfCurrentWeek(currentDate);

        LocalDateTime lastDayOfCurrentWeek = getLastDayOfCurrentWeek(currentDate);
        lastDayOfCurrentWeek = lastDayOfCurrentWeek.plusDays(1);

        List<Purchase> purchases = purchaseRepository.findByDateCreatedBetween(firstDayOfCurrentWeek, lastDayOfCurrentWeek);
        Map<LocalDateTime, BigDecimal> dailyPurchaseSummary = new HashMap<>();

        for (Purchase transaction : purchases) {
            LocalDateTime transactionDate = transaction.getDateCreated();
            BigDecimal salesAmount = transaction.getTotal();

            dailyPurchaseSummary.put(transactionDate, dailyPurchaseSummary.getOrDefault(transactionDate, BigDecimal.ZERO).add(salesAmount));
        }

        return dailyPurchaseSummary;
    }

    private static LocalDateTime getLastDayOfCurrentWeek(LocalDateTime currentDate) {
        LocalDateTime lastDayOfCurrentWeek = currentDate;
        while (lastDayOfCurrentWeek.getDayOfWeek().getValue() != 7) {
            lastDayOfCurrentWeek = lastDayOfCurrentWeek.plusDays(1);
        }
        return lastDayOfCurrentWeek;
    }

    private static LocalDateTime getFirstDayOfCurrentWeek(LocalDateTime currentDate) {
        LocalDateTime firstDayOfCurrentWeek = currentDate;
        while (firstDayOfCurrentWeek.getDayOfWeek().getValue() != 1) {
            firstDayOfCurrentWeek = firstDayOfCurrentWeek.minusDays(1);
        }
        return firstDayOfCurrentWeek;
    }

    public List<Object[]> getMonthlyProductionByFarm() {
        OffsetDateTime currentDate = OffsetDateTime.now();
        YearMonth yearMonth = YearMonth.from(currentDate.toLocalDate());
        OffsetDateTime firstDayOfMonth = yearMonth.atDay(1).atStartOfDay().atOffset(currentDate.getOffset());
        OffsetDateTime lastDayOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59).atOffset(currentDate.getOffset());
        return dashboardRepository.calculateMonthlyProductionByFarm(firstDayOfMonth, lastDayOfMonth);
    }

    public List<Object[]> getMonthlyProductionByWeek() {
        OffsetDateTime currentDate = OffsetDateTime.now();
        YearMonth yearMonth = YearMonth.from(currentDate.toLocalDate());
        OffsetDateTime firstDayOfMonth = yearMonth.atDay(1).atStartOfDay().atOffset(currentDate.getOffset());
        OffsetDateTime lastDayOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59).atOffset(currentDate.getOffset());
        return dashboardRepository.calculateMonthlyProductionByWeek(firstDayOfMonth, lastDayOfMonth);
    }

    public List<Object[]> getMonthlyProductionByAvailability() {
        OffsetDateTime currentDate = OffsetDateTime.now();
        YearMonth yearMonth = YearMonth.from(currentDate.toLocalDate());
        OffsetDateTime firstDayOfMonth = yearMonth.atDay(1).atStartOfDay().atOffset(currentDate.getOffset());
        OffsetDateTime lastDayOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59).atOffset(currentDate.getOffset());
        return dashboardRepository.getTotalProductionQuantityByMonth(firstDayOfMonth, lastDayOfMonth);
    }

}
