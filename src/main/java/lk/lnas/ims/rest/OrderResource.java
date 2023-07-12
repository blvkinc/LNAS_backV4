package lk.lnas.ims.rest;

import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lk.lnas.ims.domain.Order;
import lk.lnas.ims.model.OrderDTO;
import lk.lnas.ims.security.util.Roles;
import lk.lnas.ims.service.OrderService;
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
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderResource {

    private final OrderService service;

    @GetMapping
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    @Parameter(name = "filter", description = "Filter Query", schema = @Schema(type = "string"), in = QUERY)
    public ResponseEntity<Page<OrderDTO>> paginateOrders(
            @Parameter(hidden = true) @Filter Specification<Order> spec, @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to paginate Order");
        return ok(service.paginate(spec, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable(name = "id") final Long id) {
        return ok(service.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<Long> createOrder(@RequestBody @Valid final OrderDTO orderDTO) {
        final Long createdId = service.create(orderDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<Void> updateOrder(@PathVariable(name = "id") final Long id,
                                            @RequestBody @Valid final OrderDTO orderDTO) {
        service.update(id, orderDTO);
        return ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    @PreAuthorize("hasAuthority('" + Roles.ADMIN + "')")
    public ResponseEntity<Void> deleteOrder(@PathVariable(name = "id") final Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
