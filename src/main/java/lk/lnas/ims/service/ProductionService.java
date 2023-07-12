package lk.lnas.ims.service;

import lk.lnas.ims.domain.Farm;
import lk.lnas.ims.domain.Plant;
import lk.lnas.ims.domain.Production;
import lk.lnas.ims.model.ProductStatus;
import lk.lnas.ims.model.ProductionDTO;
import lk.lnas.ims.model.report.MonthlyProductionByFarm;
import lk.lnas.ims.model.report.ProductionQuantity;
import lk.lnas.ims.model.report.ProductionSummary;
import lk.lnas.ims.repos.FarmRepository;
import lk.lnas.ims.repos.PlantRepository;
import lk.lnas.ims.repos.ProductionRepository;
import lk.lnas.ims.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductionService {

    private final ProductionRepository repository;
    private final PlantRepository plantRepository;
    private final FarmRepository farmRepository;

    @Transactional
    public Page<ProductionDTO> paginate(Specification<Production> spec, Pageable pageable) {
        return repository.findAll(spec, pageable).map(entity -> mapToDTO(entity, new ProductionDTO()));
    }

    public ProductionDTO get(final Long id) {
        return repository.findById(id)
                .map(production -> mapToDTO(production, new ProductionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProductionDTO productionDTO) {
        final Production production = new Production();
        mapToEntity(productionDTO, production);
        return repository.save(production).getId();
    }

    public void update(final Long id, final ProductionDTO productionDTO) {
        final Production production = repository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productionDTO, production);
        repository.save(production);
    }

    public void delete(final Long id) {
        repository.deleteById(id);
    }

    private ProductionDTO mapToDTO(final Production production, final ProductionDTO productionDTO) {
        productionDTO.setId(production.getId());
        productionDTO.setName(production.getName());
        productionDTO.setDescription(production.getDescription());
        productionDTO.setProductId(production.getProductId());
        productionDTO.setQty(production.getQty());
        productionDTO.setStatus(production.getStatus());
        productionDTO.setPlant(production.getPlant() == null ? null : production.getPlant().getId());
        productionDTO.setFarm(production.getFarm() == null ? null : production.getFarm().getId());
        return productionDTO;
    }

    private Production mapToEntity(final ProductionDTO productionDTO, final Production production) {
        production.setName(productionDTO.getName());
        production.setDescription(productionDTO.getDescription());
        production.setProductId(productionDTO.getProductId());
        production.setQty(productionDTO.getQty());
        production.setStatus(productionDTO.getStatus());
        final Plant plant = productionDTO.getPlant() == null ? null : plantRepository.findById(productionDTO.getPlant())
                .orElseThrow(() -> new NotFoundException("plant not found"));
        production.setPlant(plant);
        final Farm farm = productionDTO.getFarm() == null ? null : farmRepository.findById(productionDTO.getFarm())
                .orElseThrow(() -> new NotFoundException("farm not found"));
        production.setFarm(farm);
        return production;
    }

    public List<ProductionQuantity> getPastSixMonthsProductionQuantities() {
        OffsetDateTime currentDate = OffsetDateTime.now();
        OffsetDateTime sixMonthsAgo = currentDate.minusMonths(6).withDayOfMonth(1).withOffsetSameInstant(currentDate.getOffset());
        OffsetDateTime firstDayOfMonth = currentDate.withDayOfMonth(1).withOffsetSameInstant(currentDate.getOffset());

        List<Object[]> results = repository.findMonthlyProductionQuantities(sixMonthsAgo, firstDayOfMonth);
        return mapProductionQuantities(results);
    }

    public List<ProductionQuantity> getPastFourWeeksProductionQuantities() {
        OffsetDateTime currentDate = OffsetDateTime.now();
        OffsetDateTime fourWeeksAgo = currentDate.minusWeeks(4).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).withOffsetSameInstant(currentDate.getOffset());
        OffsetDateTime lastDayOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).withOffsetSameInstant(currentDate.getOffset());

        List<Object[]> results = repository.findWeeklyProductionQuantities(fourWeeksAgo, lastDayOfWeek);
        return mapProductionQuantities(results);
    }

    private List<ProductionQuantity> mapProductionQuantities(List<Object[]> results) {
        List<ProductionQuantity> quantities = new ArrayList<>();
        for (Object[] result : results) {
            String period = (String) result[0];
            Long totalQty = (Long) result[1];
            quantities.add(new ProductionQuantity(period, totalQty));
        }
        return quantities;
    }

    public List<ProductionSummary> getProductionSummaryByStatus() {
        List<Object[]> results = repository.findProductionSummaryByStatus();
        return mapProductionSummary(results);
    }

    private List<ProductionSummary> mapProductionSummary(List<Object[]> results) {
        List<ProductionSummary> summaries = new ArrayList<>();
        for (Object[] result : results) {
            ProductStatus status = (ProductStatus) result[0];
            Long count = (Long) result[1];
            summaries.add(new ProductionSummary(status, count));
        }
        return summaries;
    }

    public List<MonthlyProductionByFarm> getMonthlyProductionByFarm() {
        OffsetDateTime currentDate = OffsetDateTime.now();
        OffsetDateTime sixMonthsAgo = currentDate.minusMonths(6).withDayOfMonth(1);
        OffsetDateTime firstDayOfMonth = currentDate.withDayOfMonth(1);

        List<Object[]> results = repository.findMonthlyProductionByFarm(sixMonthsAgo, firstDayOfMonth);
        return mapMonthlyProductionByFarm(results);
    }

    private List<MonthlyProductionByFarm> mapMonthlyProductionByFarm(List<Object[]> results) {
        List<MonthlyProductionByFarm> productionList = new ArrayList<>();
        for (Object[] result : results) {
            String month = (String) result[0];
            Farm farm = (Farm) result[1];
            Long totalQty = (Long) result[2];
            productionList.add(new MonthlyProductionByFarm(month, farm, totalQty));
        }
        return productionList;
    }

}
