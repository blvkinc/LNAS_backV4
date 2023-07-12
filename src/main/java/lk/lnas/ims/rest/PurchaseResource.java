package lk.lnas.ims.rest;

import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lk.lnas.ims.domain.Purchase;
import lk.lnas.ims.model.PurchaseDTO;
import lk.lnas.ims.security.util.Roles;
import lk.lnas.ims.service.PurchaseService;
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
@RequestMapping(value = "/api/purchases", produces = MediaType.APPLICATION_JSON_VALUE)
public class PurchaseResource {

    private final PurchaseService service;

    @GetMapping
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    @Parameter(name = "filter", description = "Filter Query", schema = @Schema(type = "string"), in = QUERY)
    public ResponseEntity<Page<PurchaseDTO>> paginatePurchases(
            @Parameter(hidden = true) @Filter Specification<Purchase> spec, @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to paginate Purchase");
        return ok(service.paginate(spec, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<PurchaseDTO> getPurchase(@PathVariable(name = "id") final Long id) {
        return ok(service.get(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPurchase(@RequestBody @Valid final PurchaseDTO purchaseDTO) {
        final Long createdId = service.create(purchaseDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<Void> updatePurchase(@PathVariable(name = "id") final Long id,
                                               @RequestBody @Valid final PurchaseDTO purchaseDTO) {
        service.update(id, purchaseDTO);
        return ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<Void> deletePurchase(@PathVariable(name = "id") final Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
