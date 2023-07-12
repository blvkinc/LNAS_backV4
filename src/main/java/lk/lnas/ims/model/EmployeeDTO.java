package lk.lnas.ims.model;

import jakarta.validation.constraints.NotEmpty;
import lk.lnas.ims.security.model.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class EmployeeDTO {

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

    private List<Long> farms;

    private UserDTO userAccount;

}
