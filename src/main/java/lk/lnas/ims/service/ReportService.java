package lk.lnas.ims.service;

import lk.lnas.ims.domain.Employee;
import lk.lnas.ims.model.report.*;
import lk.lnas.ims.repos.ReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository repository;

    public List<SalesSummaryDTO> getSalesSummary(OffsetDateTime startDate,OffsetDateTime endDate) {
        return repository.getSalesSummary(startDate,endDate).stream().map(row -> {
            SalesSummaryDTO summaryDTO = new SalesSummaryDTO();
            summaryDTO.setProductId((Long) row[0]);
            summaryDTO.setProductName((String) row[1]);
            summaryDTO.setOrderQty((Long) row[2]);
            summaryDTO.setPurchaseQty((Long) row[3]);
            summaryDTO.setUnitPrice((BigDecimal) row[4]);
            summaryDTO.setUnitCost((BigDecimal) row[5]);
            summaryDTO.setUnitProfit(summaryDTO.getUnitPrice().subtract(summaryDTO.getUnitCost()));
            summaryDTO.setTotalRevenue(summaryDTO.getUnitPrice().multiply(BigDecimal.valueOf(summaryDTO.getOrderQty())));
            summaryDTO.setDate((OffsetDateTime) row[6]);
            return summaryDTO;
        }).toList();
    }

    public List<ProductionSummaryDTO> getProductionSummary(OffsetDateTime startDate,OffsetDateTime endDate) {
        return repository.getProductionSummary(startDate,endDate).stream().map(row -> {
            ProductionSummaryDTO summaryDTO = new ProductionSummaryDTO();
            summaryDTO.setActualProduction((Long) row[0]);
            summaryDTO.setEstimatedProduction((Long) row[1]);
            summaryDTO.setFarmId((Long) row[2]);
            summaryDTO.setPlantName((String) row[3]);
            summaryDTO.setDifference(summaryDTO.getEstimatedProduction() - summaryDTO.getActualProduction());
            return summaryDTO;
        }).toList();
    }

    public List<PurchaseSummaryDTO> getPurchaseSummary(OffsetDateTime startDate,OffsetDateTime endDate) {
        return repository.getPurchaseSummary(startDate,endDate).stream().map(row -> {
            PurchaseSummaryDTO summaryDTO = new PurchaseSummaryDTO();
            summaryDTO.setWeek((Integer) row[2]);
            summaryDTO.setProductName((String) row[7]);
            summaryDTO.setQty((Long) row[8]);
            summaryDTO.setUnitPrice((BigDecimal) row[9]);
            summaryDTO.setTotalPrice(summaryDTO.getUnitPrice().multiply(BigDecimal.valueOf(summaryDTO.getQty())));
            return summaryDTO;
        }).toList();
    }

    public List<SalarySummaryDTO> getSalarySummary(OffsetDateTime startDate,OffsetDateTime endDate) {
        return repository.getSalarySummary(startDate,endDate).stream().map(row -> {
            Employee employee = (Employee) row[0];
            BigDecimal amount = (BigDecimal) row[1];

            SalarySummaryDTO summaryDTO = new SalarySummaryDTO();
            summaryDTO.setEmployeeId(employee.getId());
            summaryDTO.setEmployeeName(employee.getFirstName().concat(" ").concat(employee.getLastName()));
            summaryDTO.setEmployeeEmail(employee.getEmail());
            summaryDTO.setAmount(amount);
            return summaryDTO;
        }).toList();
    }

    public List<FarmSummaryDTO> getFarmSummary(OffsetDateTime startDate,OffsetDateTime endDate) {
        return repository.getFarmSummary(startDate,endDate).stream().map(row -> {
            FarmSummaryDTO summaryDTO = new FarmSummaryDTO();
            summaryDTO.setFarmId((Long) row[2]);
            summaryDTO.setPlantName((String) row[3]);
            summaryDTO.setEstimatedProduction((Long) row[1]);
            summaryDTO.setActualProduction((Long) row[0]);
            summaryDTO.setDifference(summaryDTO.getEstimatedProduction() - summaryDTO.getActualProduction());
            return summaryDTO;
        }).toList();
    }
}
