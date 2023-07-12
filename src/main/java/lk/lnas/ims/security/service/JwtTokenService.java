package lk.lnas.ims.security.service;

import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;
import lk.lnas.ims.model.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_ROLES = "roles";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.accessTokenLife}")
    private Long accessTokenLife;
    @Value("${jwt.refreshTokenLife}")
    private Long refreshTokenLife;
    @Value("jwt.issuer")
    private String issuer;

    private String generateToken(HashMap<String, String> claims, Long expiration, String subject) {
        Signer signer = HMACSigner.newSHA256Signer(secret);

        JWT jwt = new JWT().setIssuer(issuer)
                .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .setSubject(subject)
                .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(expiration));

        claims.keySet().forEach(k -> {
            if (claims.get(k) != null) {
                jwt.addClaim(k, claims.get(k));
            }
        });
        return JWT.getEncoder().encode(jwt, signer);
    }

    private HashMap<String, String> getClaimsFromToken(String token) {
        Verifier verifier = HMACVerifier.newVerifier(secret);
        JWT jwt = JWT.getDecoder().decode(token, verifier);
        HashMap<String, String> claims = new HashMap<>();
        if (jwt != null) {
            jwt.getAllClaims().forEach((k, v) -> claims.put(k, v.toString()));
        }
        return claims;
    }

    public String getUserNameFromToken(String token) {
        String username;
        try {
            HashMap<String, String> claims = getClaimsFromToken(token);
            username = claims.get(CLAIM_KEY_USERNAME);
        } catch (Exception e) {
            log.error("Error while getting username from token", e);
            username = null;
        }
        return username;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date(System.currentTimeMillis()));
    }

    public Date getExpiredDateFromToken(String token) {
        Verifier verifier = HMACVerifier.newVerifier(secret);
        JWT jwt = JWT.getDecoder().decode(token, verifier);
        return new Date(jwt.expiration.toInstant().toEpochMilli());
    }

    public String generateAccessToken(UserDetails userDetails, Role role) {
        HashMap<String, String> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date().toString());
        claims.put(CLAIM_KEY_ROLES, role.toString());
        return generateToken(claims, accessTokenLife, userDetails.getUsername());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        HashMap<String, String> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date().toString());
        return generateToken(claims, refreshTokenLife, userDetails.getUsername());
    }

}
