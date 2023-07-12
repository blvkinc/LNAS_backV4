package lk.lnas.ims.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration()
@ConfigurationProperties("jwt")
public class JWTConfig {
    private String secret;
    private String issuer;
    private Long expiration;
    private String header;
    private String prefix;
    private String suffix;
    private Long accessTokenLife;
    private Long refreshTokenLife;
}
