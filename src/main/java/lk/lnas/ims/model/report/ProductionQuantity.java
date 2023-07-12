package lk.lnas.ims.model.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductionQuantity {
    private String period;
    private Long totalQuantity;
}
