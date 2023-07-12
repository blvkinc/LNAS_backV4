package lk.lnas.ims.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
public class PlantDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String productId;

    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "49.08")
    private BigDecimal salesPrice;

    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "84.08")
    private BigDecimal purchasePrice;

    @NotNull
    private Long qtyAtHand;

    @NotNull
    private Long qtyPotential;

    @Size(max = 255)
    private String scientificName;

    @Size(max = 255)
    private String description;

    @NotNull
    private PlantStatus status;

}
