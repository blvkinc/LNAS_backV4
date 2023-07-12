package lk.lnas.ims.rest;

import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lk.lnas.ims.domain.Employee;
import lk.lnas.ims.model.EmployeeDTO;
import lk.lnas.ims.security.util.Roles;
import lk.lnas.ims.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/employees", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeResource {

    private final EmployeeService service;

    @GetMapping
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    @Parameter(name = "filter", description = "Filter Query", schema = @Schema(type = "string"), in = QUERY)
    public ResponseEntity<Page<EmployeeDTO>> paginateEmployees(
            @Parameter(hidden = true) @Filter Specification<Employee> spec, @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to paginate Employee");
        return ok(service.paginate(spec, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable(name = "id") final Long id) {
        return ok(service.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<Long> createEmployee(@RequestBody @Valid final EmployeeDTO employeeDTO) {
        final Long createdId = service.create(employeeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<Void> updateEmployee(@PathVariable(name = "id") final Long id,
                                               @RequestBody @Valid final EmployeeDTO employeeDTO) {
        service.update(id, employeeDTO);
        return ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable(name = "id") final Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
