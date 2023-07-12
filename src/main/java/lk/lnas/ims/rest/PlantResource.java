package lk.lnas.ims.rest;

import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lk.lnas.ims.domain.Plant;
import lk.lnas.ims.model.PlantDTO;
import lk.lnas.ims.security.util.Roles;
import lk.lnas.ims.service.PlantService;
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
@RequestMapping(value = "/api/plants", produces = MediaType.APPLICATION_JSON_VALUE)
public class PlantResource {

    private final PlantService service;

    @GetMapping
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAnyAuthority('"+Roles.MODERATOR+"') or hasAnyAuthority('"+Roles.CUSTOMER+"')")
    @Parameter(name = "filter", description = "Filter Query", schema = @Schema(type = "string"), in = ParameterIn.QUERY)
    public ResponseEntity<Page<PlantDTO>> paginatePlants(
            @Parameter(hidden = true) @Filter Specification<Plant> spec, @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to paginate Suppliers");
        return ResponseEntity.ok(service.paginate(spec, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAnyAuthority('"+Roles.MODERATOR+"') ")
    public ResponseEntity<PlantDTO> getPlant(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAnyAuthority('"+Roles.MODERATOR+"') ")
    public ResponseEntity<Long> createPlant(@RequestBody @Valid final PlantDTO plantDTO) {
        final Long createdId = service.create(plantDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAnyAuthority('"+Roles.MODERATOR+"') ")
    public ResponseEntity<Void> updatePlant(@PathVariable(name = "id") final Long id,
                                            @RequestBody @Valid final PlantDTO plantDTO) {
        service.update(id, plantDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "') or hasAnyAuthority('"+Roles.MODERATOR+"') ")
    public ResponseEntity<Void> deletePlant(@PathVariable(name = "id") final Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
