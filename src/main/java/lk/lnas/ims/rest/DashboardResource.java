package lk.lnas.ims.rest;

import lk.lnas.ims.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
public class DashboardResource {
    private final DashboardService dashboardService;

    @GetMapping("/sales/total-sales")
    public ResponseEntity<BigDecimal> getTotalSales() {
        BigDecimal totalSales = dashboardService.getTotalSales();
        return ResponseEntity.ok(totalSales);
    }

    @GetMapping("/sales/total-production")
    public ResponseEntity<BigDecimal> getTotalProduction() {
        BigDecimal totalProduction = dashboardService.getTotalProduction();
        return ResponseEntity.ok(totalProduction);
    }

    @GetMapping("/sales/total-count")
    public ResponseEntity<BigDecimal> getTotalCount() {
        BigDecimal totalCount = dashboardService.getTotalCount();
        return ResponseEntity.ok(totalCount);
    }

    @GetMapping("/Production/total-farm")
    public ResponseEntity<BigDecimal> getTotalFarms() {
        BigDecimal totalCount = dashboardService.getTotalFarms();
        return ResponseEntity.ok(totalCount);
    }

    @GetMapping("/sales/monthly-sales")
    public ResponseEntity<BigDecimal> getMonthlySales() {
        BigDecimal monthlySales = dashboardService.getMonthlySales();
        return ResponseEntity.ok(monthlySales);
    }

    @GetMapping("/sales/monthly-production")
    public ResponseEntity<BigDecimal> getMonthlyProduction() {
        BigDecimal monthlyProduction = dashboardService.getMonthlyProduction();
        return ResponseEntity.ok(monthlyProduction);
    }

    @GetMapping("/plant/monthly-count")
    public ResponseEntity<BigDecimal> getMonthlyPlantCount() {
        BigDecimal monthlyPlantCount = dashboardService.getMonthlyPlantCount();
        return ResponseEntity.ok(monthlyPlantCount);
    }

    @GetMapping("/farms/active-count")
    public ResponseEntity<BigDecimal> getActiveFarmsCount() {
        BigDecimal activeFarmsCount = dashboardService.getActiveFarmsCount();
        return ResponseEntity.ok(activeFarmsCount);
    }

    // Chart Endpoints
    @GetMapping("/sales/weekly-sales")
    public ResponseEntity<Map<LocalDateTime, BigDecimal>> getWeeklySales() {
        return ResponseEntity.ok(dashboardService.getWeeklySales());
    }

    @GetMapping("/purchase/weekly-purchase")
    public ResponseEntity<Map<LocalDateTime, BigDecimal>> getWeeklyPurchase() {
        return ResponseEntity.ok(dashboardService.getWeeklyPurchase());
    }

    @GetMapping("/monthly-production-by-farm")
    public ResponseEntity<List<Object[]>> getMonthlyProductionByFarm() {
        List<Object[]> monthlyProductionByFarm = dashboardService.getMonthlyProductionByFarm();
        return ResponseEntity.ok(monthlyProductionByFarm);
    }

    @GetMapping("/production/monthly-production-by-week")
    public ResponseEntity<List<Object[]>> getMonthlyProductionByWeek() {
        List<Object[]> monthlyProductionByWeek = dashboardService.getMonthlyProductionByWeek();
        return ResponseEntity.ok(monthlyProductionByWeek);
    }

    @GetMapping("/production/monthly-production-by-availability")
    public ResponseEntity<List<Object[]>> getMonthlyProductionByAvailability() {
        List<Object[]> monthlyProductionByAvailability = dashboardService.getMonthlyProductionByAvailability();
        return ResponseEntity.ok(monthlyProductionByAvailability);
    }

}
