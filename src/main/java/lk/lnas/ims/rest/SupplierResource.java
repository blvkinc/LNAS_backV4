package lk.lnas.ims.rest;

import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lk.lnas.ims.domain.Supplier;
import lk.lnas.ims.model.SupplierDTO;
import lk.lnas.ims.security.util.Roles;
import lk.lnas.ims.service.SupplierService;
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
@RequestMapping(value = "/api/suppliers", produces = MediaType.APPLICATION_JSON_VALUE)
public class SupplierResource {

    private final SupplierService service;

    @GetMapping
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAuthority('" + Roles.MODERATOR + "') or hasAuthority('" + Roles.SUPPLIER + "')")
    @Parameter(name = "filter", description = "Filter Query", schema = @Schema(type = "string"), in = QUERY)
    public ResponseEntity<Page<SupplierDTO>> paginateSuppliers(
            @Parameter(hidden = true) @Filter Specification<Supplier> spec, @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to paginate Supplier");
        return ok(service.paginate(spec, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAuthority('" + Roles.MODERATOR + "') or hasAuthority('" + Roles.SUPPLIER + "')")
    public ResponseEntity<SupplierDTO> getSupplier(@PathVariable(name = "id") final Long id) {
        return ok(service.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAuthority('" + Roles.MODERATOR + "') ")
    public ResponseEntity<Long> createSupplier(@RequestBody @Valid final SupplierDTO supplierDTO) {
        final Long createdId = service.create(supplierDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAuthority('" + Roles.MODERATOR + "') or hasAuthority('" + Roles.SUPPLIER + "')")
    public ResponseEntity<Void> updateSupplier(@PathVariable(name = "id") final Long id,
                                               @RequestBody @Valid final SupplierDTO supplierDTO) {
        service.update(id, supplierDTO);
        return ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAuthority('" + Roles.MODERATOR + "') ")
    public ResponseEntity<Void> deleteSupplier(@PathVariable(name = "id") final Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
