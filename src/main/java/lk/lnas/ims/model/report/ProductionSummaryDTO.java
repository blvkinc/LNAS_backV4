package lk.lnas.ims.model.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductionSummaryDTO {
    private Long farmId;
    private String plantName;
    private Long estimatedProduction;
    private Long actualProduction;
    private Long difference;
}
