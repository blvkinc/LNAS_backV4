package lk.lnas.ims.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemDTO {
    private Long id;
    @NotNull
    private Long plant;
    @NotNull
    private BigDecimal price;
    @NotNull
    private BigDecimal discount;
    @NotNull
    private Long qty;
    private String description;
}
