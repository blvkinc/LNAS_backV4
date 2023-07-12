package lk.lnas.ims.security.rest;

import jakarta.servlet.http.HttpServletResponse;
import lk.lnas.ims.model.Role;
import lk.lnas.ims.security.entity.User;
import lk.lnas.ims.security.exception.ApplicationSecurityException;
import lk.lnas.ims.security.model.*;
import lk.lnas.ims.security.service.JwtTokenService;
import lk.lnas.ims.security.service.UserService;
import lk.lnas.ims.security.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthResource {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService tokenService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );

            UserDTO user = (UserDTO) authenticate.getPrincipal();

            if (Boolean.FALSE.equals(user.getIsApproved())) {
                throw new ApplicationSecurityException(HttpStatus.UNAUTHORIZED, "NOT_APPROVED", "User is not approved");
            }

            AuthResponseDTO authResponseDTO = generateAuthResponse(user, user.getRole());
            authResponseDTO.setUsername(user.getUsername());
            authResponseDTO.setFirstName(user.getFirstName());
            authResponseDTO.setLastName(user.getLastName());
            authResponseDTO.setRole(user.getRole().toString());
            return ResponseEntity.ok().body(authResponseDTO);
        } catch (BadCredentialsException e) {
            throw new ApplicationSecurityException(HttpStatus.NOT_FOUND, "INVALID_CREDENTIALS", "Invalid username or password");
        }
    }

    @PostMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO, @CurrentUser User user) {
        boolean matches = passwordEncoder.matches(updatePasswordDTO.getOldPassword(), user.getPassword());
        if (!matches) {
            throw new ApplicationSecurityException(HttpStatus.NOT_FOUND, "INVALID_CREDENTIALS", "Invalid password");
        }
        userService.updatePassword(user.getId(), updatePasswordDTO.getNewPassword());
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/update-profile")
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateProfileRequestDTO userDTO, @CurrentUser User user) {
        if (!userDTO.getEmail().equals(user.getEmail())) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(null);
        }
        userService.updateProfile(user, userDTO);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> signUp(@RequestBody UserDTO user) {
        return new ResponseEntity<>(userService.signUp(user), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@CurrentUser User user) {
        return ResponseEntity.ok().body(userService.mapToDTO(user, new UserDTO()));
    }

    @PostMapping("/token-refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@RequestBody TokenRequestDTO requestDTO) {
        try {
            UserDetails userDetails = userService.loadUserByUsername(
                    tokenService.getUserNameFromToken(requestDTO.getRefreshToken())
            );
            Role role = userService.getRole(userDetails.getUsername());
            boolean isValidToken = tokenService.validateToken(requestDTO.getRefreshToken(), userDetails);
            if (isValidToken) {
                return ResponseEntity.ok().body(generateAuthResponse(userDetails, role));
            } else {
                throw new ApplicationSecurityException(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Invalid token");
            }
        } catch (Exception e) {
            throw new ApplicationSecurityException(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Invalid token");
        }
    }

    @GetMapping("/is-email-available")
    public ResponseEntity<Void> isEmailAvailable(@RequestParam("email") String email) {
        Boolean emailAvailable = userService.isEmailAvailable(email);
        if (Boolean.TRUE.equals(emailAvailable)) {
            return ResponseEntity.ok().body(null);
        }else {
            throw new ApplicationSecurityException(HttpStatus.BAD_REQUEST, "EMAIL_NOT_AVAILABLE", "Email is not available");
        }
    }
    private AuthResponseDTO generateAuthResponse(UserDetails userDetails, Role role) {
        AuthResponseDTO authResponse = new AuthResponseDTO();
        authResponse.setAccessToken(tokenService.generateAccessToken(userDetails, role));
        authResponse.setRefreshToken(tokenService.generateRefreshToken(userDetails));
        authResponse.setExpiresIn(tokenService.getExpiredDateFromToken(authResponse.getAccessToken()));
        return authResponse;
    }
}
