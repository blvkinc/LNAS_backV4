package lk.lnas.ims.model.report;

import lk.lnas.ims.model.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionSummary {
    private ProductStatus status;
    private Long count;
}
