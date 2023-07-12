package lk.lnas.ims.rest;

import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lk.lnas.ims.domain.Production;
import lk.lnas.ims.model.ProductionDTO;
import lk.lnas.ims.security.util.Roles;
import lk.lnas.ims.service.ProductionService;
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
@RequestMapping(value = "/api/productions", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductionResource {

    private final ProductionService service;

    @GetMapping
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAnyAuthority('"+Roles.MODERATOR+"') or hasAnyAuthority('"+Roles.EMPLOYEE+"') or hasAnyAuthority('"+Roles.CUSTOMER+"') or hasAnyAuthority('"+Roles.SUPPLIER+"')")
    @Parameter(name = "filter", description = "Filter Query", schema = @Schema(type = "string"), in = QUERY)
    public ResponseEntity<Page<ProductionDTO>> paginateProductions(
            @Parameter(hidden = true) @Filter Specification<Production> spec, @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to paginate Production");
        return ok(service.paginate(spec, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAnyAuthority('"+Roles.MODERATOR+"')")
    public ResponseEntity<ProductionDTO> getProduction(@PathVariable(name = "id") final Long id) {
        return ok(service.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAnyAuthority('"+Roles.MODERATOR+"')")
    public ResponseEntity<Long> createProduction(
            @RequestBody @Valid final ProductionDTO productionDTO) {
        final Long createdId = service.create(productionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAnyAuthority('"+Roles.MODERATOR+"') or hasAnyAuthority('"+Roles.EMPLOYEE+"')")
    public ResponseEntity<Void> updateProduction(@PathVariable(name = "id") final Long id,
                                                 @RequestBody @Valid final ProductionDTO productionDTO) {
        service.update(id, productionDTO);
        return ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAnyAuthority('"+Roles.MODERATOR+"')")
    public ResponseEntity<Void> deleteProduction(@PathVariable(name = "id") final Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
