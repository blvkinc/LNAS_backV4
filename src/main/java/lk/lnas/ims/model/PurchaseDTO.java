package lk.lnas.ims.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
public class PurchaseDTO {

    private Long id;

    private PurchaseStatus status;

    @Size(max = 255)
    private String type;

    private BigDecimal subTotal;

    private BigDecimal discount;

    private BigDecimal tax;

    private BigDecimal shipping;

    private BigDecimal total;

    private List<PurchaseItemDTO> items;

    @NotNull
    private String documentId;

}
