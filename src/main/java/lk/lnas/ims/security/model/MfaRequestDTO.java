package lk.lnas.ims.security.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MfaRequestDTO {
    private String password;
    private String code;
}
