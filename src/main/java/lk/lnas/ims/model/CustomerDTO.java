package lk.lnas.ims.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomerDTO {

    private Long id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    private String email;

    private String phone;

    @NotEmpty
    private String address;

    private EmployeeStatus status;

}
