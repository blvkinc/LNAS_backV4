package lk.lnas.ims.security.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequestDTO {
    private String refreshToken;
}
