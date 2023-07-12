package lk.lnas.ims.model.report;

import lk.lnas.ims.domain.Farm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyProductionByFarm {
    private String month;
    private Farm farm;
    private Long totalQty;
}

