package lk.lnas.ims.rest;

import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lk.lnas.ims.domain.Farm;
import lk.lnas.ims.model.FarmDTO;
import lk.lnas.ims.security.util.Roles;
import lk.lnas.ims.service.FarmService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/farms", produces = MediaType.APPLICATION_JSON_VALUE)
public class FarmResource {

    private final FarmService service;

    @GetMapping
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAnyAuthority('"+Roles.MODERATOR+"') or hasAnyAuthority('"+Roles.EMPLOYEE+"')")
    @Parameter(name = "filter", description = "Filter Query", schema = @Schema(type = "string"), in = ParameterIn.QUERY)
    public ResponseEntity<Page<FarmDTO>> paginateFarms(
            @Parameter(hidden = true) @Filter Specification<Farm> spec, @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to paginate Suppliers");
        return ResponseEntity.ok(service.paginate(spec, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FarmDTO> getFarm(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAnyAuthority('"+Roles.MODERATOR+"')")
    public ResponseEntity<Long> createFarm(@RequestBody @Valid final FarmDTO farmDTO) {
        final Long createdId = service.create(farmDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAnyAuthority('"+Roles.MODERATOR+"') or hasAnyAuthority('"+Roles.EMPLOYEE+"')")
    public ResponseEntity<Void> updateFarm(@PathVariable(name = "id") final Long id,
                                           @RequestBody @Valid final FarmDTO farmDTO) {
        service.update(id, farmDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAnyAuthority('"+Roles.MODERATOR+"')")
    public ResponseEntity<Void> deleteFarm(@PathVariable(name = "id") final Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
