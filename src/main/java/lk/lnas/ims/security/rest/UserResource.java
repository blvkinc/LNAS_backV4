package lk.lnas.ims.security.rest;

import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lk.lnas.ims.security.entity.User;
import lk.lnas.ims.security.model.UserDTO;
import lk.lnas.ims.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserResource {
    private final UserService service;

    @GetMapping
    @Parameter(name = "filter", description = "Filter Query", schema = @Schema(type = "string"), in = QUERY)
    public ResponseEntity<Page<UserDTO>> paginateUsers(
            @Parameter(hidden = true) @Filter Specification<User> spec, @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to paginate User");
        return ok(service.paginate(spec, pageable));
    }

    @GetMapping("/by-id/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") final Long id) {
        return ok(service.get(id));
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<UserDTO> getUserbyEmail(@PathVariable("email") final String email) {
        return ok(service.getUserbyEmail(email));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user) {
        return new ResponseEntity<>(service.create(user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable("id") final Long id, @RequestBody UserDTO user) {
        service.update(id, user);
        return ok().build();
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveUser(@PathVariable("id") final Long id) {
        service.approve(id);
        return ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") final Long id) {
        service.delete(id);
        return ok().build();
    }

}

