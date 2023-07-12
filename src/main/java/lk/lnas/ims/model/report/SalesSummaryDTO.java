package lk.lnas.ims.model.report;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class SalesSummaryDTO {
    private Long productId;
    private String productName;
    private Long orderQty;
    private Long purchaseQty;
    private BigDecimal unitPrice;
    private BigDecimal unitCost;
    private BigDecimal unitProfit;
    private BigDecimal totalRevenue;
    private OffsetDateTime date;

}
