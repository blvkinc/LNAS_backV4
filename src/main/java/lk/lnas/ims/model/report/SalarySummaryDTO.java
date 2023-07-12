package lk.lnas.ims.model.report;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SalarySummaryDTO {
    private Long employeeId;
    private String employeeName;
    private String employeeEmail;
    private BigDecimal amount;
}
