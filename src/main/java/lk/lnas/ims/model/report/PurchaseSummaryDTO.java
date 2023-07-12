package lk.lnas.ims.model.report;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PurchaseSummaryDTO {
    private Integer week;
    private String productName;
    private Long qty;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
