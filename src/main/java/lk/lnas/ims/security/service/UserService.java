package lk.lnas.ims.security.service;

import lk.lnas.ims.model.Role;
import lk.lnas.ims.security.entity.User;
import lk.lnas.ims.security.model.UpdateProfileRequestDTO;
import lk.lnas.ims.security.model.UserDTO;
import lk.lnas.ims.security.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.function.Function;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Value("${initializer.username}")
    private String adminUsername;
    @Value("${initializer.password}")
    private String adminPassword;

    @Value("${initializer.address}")
    private String adminAddress;

    @Bean
    public Function<UserDetails, User> fetchCurrentUser() {
        return user -> getUser(user.getUsername());
    }

    @Transactional
    public Page<UserDTO> paginate(Specification<User> spec, Pageable pageable) {
        return repository.findAll(spec, pageable).map(entity -> mapToDTO(entity, new UserDTO()));
    }

    public UserDTO get(final Long id) {
        return repository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public User getByUsername(final String username) {
        return repository.findByUsername(username);
    }

    public UserDTO create(final UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        final User user = new User();
        mapToEntity(userDTO, user);

        user.setIsTotpVerified(false);
        user.setIsEmailVerified(false);
        user.setIsPhoneVerified(false);
        user.setIsMfaEnabled(false);
        user.setIsApproved(true);
        user.setIsBanned(false);
        user.setIsTempPassword(false);
        return mapToDTO(repository.save(user), new UserDTO());
    }

    public UserDTO signUp(final UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        final User user = new User();
        mapToEntity(userDTO, user);
        user.setUsername(user.getEmail());
        user.setIsTotpVerified(false);
        user.setIsEmailVerified(false);
        user.setIsPhoneVerified(false);
        user.setIsMfaEnabled(false);
        user.setIsApproved(false);
        user.setIsBanned(false);
        user.setIsTempPassword(false);
        return mapToDTO(repository.save(user), new UserDTO());
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(userDTO, user);
        repository.save(user);
    }

    public void delete(final Long id) {
        repository.deleteById(id);
    }

    public UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setAddress(user.getAddress());
        userDTO.setIsEmailVerified(user.getIsEmailVerified());
        userDTO.setIsPhoneVerified(user.getIsPhoneVerified());
        userDTO.setIsTempPassword(user.getIsTempPassword());
        userDTO.setIsBanned(user.getIsBanned());
        userDTO.setIsTotpVerified(user.getIsTotpVerified());
        userDTO.setIsMfaEnabled(user.getIsMfaEnabled());
        userDTO.setIsApproved(user.getIsApproved());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    public void mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setIsEmailVerified(userDTO.getIsEmailVerified());
        user.setIsPhoneVerified(userDTO.getIsPhoneVerified());
        user.setIsTempPassword(userDTO.getIsTempPassword());
        user.setIsTotpVerified(userDTO.getIsTotpVerified());
        user.setIsMfaEnabled(userDTO.getIsMfaEnabled());
        user.setIsBanned(userDTO.getIsBanned());
        user.setIsApproved(userDTO.getIsApproved());
        user.setRole(userDTO.getRole());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        UserDTO dto = mapToDTO(user, new UserDTO());
        dto.setPassword(user.getPassword());
        return dto;
    }

    public User getUser(String username) {
        return repository.findByUsername(username);
    }

    public void createAdminIfNotExists() {

        List<User> admins = repository.findByRole(Role.SUPER_ADMIN);
        if (admins.isEmpty()) {
            final User user = repository.findByUsername(adminUsername);
            if (user == null) {
                final UserDTO userDTO = new UserDTO();
                userDTO.setUsername(adminUsername);
                userDTO.setPassword(adminPassword);
                userDTO.setFirstName("Super");
                userDTO.setLastName("Admin");
                userDTO.setEmail(adminUsername);
                userDTO.setAddress(adminAddress);
                userDTO.setPhone("+94 xxxxxxxx");
                userDTO.setIsBanned(false);
                userDTO.setIsApproved(true);
                userDTO.setIsTempPassword(true);
                userDTO.setRole(Role.ADMIN);
                create(userDTO);
            }
        }
    }

    public void updatePassword(Long id, String newPassword) {
        repository.findById(id).ifPresent(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            repository.save(user);
        });
    }

    public Role getRole(String username) {
        return getByUsername(username).getRole();
    }

    public void approve(Long id) {
        repository.findById(id).ifPresent(user -> {
            user.setIsApproved(true);
            repository.save(user);
        });
    }

    public void updateProfile(User user, UpdateProfileRequestDTO userDTO) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());

        repository.save(user);
    }
}
