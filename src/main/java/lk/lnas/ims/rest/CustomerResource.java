package lk.lnas.ims.rest;

import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lk.lnas.ims.domain.Customer;
import lk.lnas.ims.model.CustomerDTO;
import lk.lnas.ims.security.util.Roles;
import lk.lnas.ims.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerResource {

    private final CustomerService service;

    @GetMapping
    @Parameter(name = "filter", description = "Filter Query", schema = @Schema(type = "string"), in = QUERY)
    @PreAuthorize("hasAuthority('" + Roles.MODERATOR + "') or hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<Page<CustomerDTO>> paginateCustomers(
            @Parameter(hidden = true) @Filter Specification<Customer> spec, @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to paginate Customer");
        return ok(service.paginate(spec, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Roles.MODERATOR + "') or hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable(name = "id") final Long id) {
        return ok(service.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @PreAuthorize("hasAuthority('" + Roles.MODERATOR + "') or hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<Long> createCustomer(@RequestBody @Valid final CustomerDTO customerDTO) {
        final Long createdId = service.create(customerDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Roles.MODERATOR + "') or hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<Void> updateCustomer(@PathVariable(name = "id") final Long id,
                                               @RequestBody @Valid final CustomerDTO customerDTO) {
        service.update(id, customerDTO);
        return ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("hasAuthority('" + Roles.MODERATOR + "') or hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable(name = "id") final Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
