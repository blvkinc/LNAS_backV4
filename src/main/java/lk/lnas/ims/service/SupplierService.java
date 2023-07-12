package lk.lnas.ims.service;

import lk.lnas.ims.domain.Supplier;
import lk.lnas.ims.model.SupplierDTO;
import lk.lnas.ims.repos.SupplierRepository;
import lk.lnas.ims.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository repository;

    @Transactional
    public Page<SupplierDTO> paginate(Specification<Supplier> spec, Pageable pageable) {
        return repository.findAll(spec, pageable).map(entity -> mapToDTO(entity, new SupplierDTO()));
    }

    public SupplierDTO get(final Long id) {
        return repository.findById(id)
                .map(supplier -> mapToDTO(supplier, new SupplierDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final SupplierDTO supplierDTO) {
        final Supplier supplier = new Supplier();
        mapToEntity(supplierDTO, supplier);
        return repository.save(supplier).getId();
    }

    public void update(final Long id, final SupplierDTO supplierDTO) {
        final Supplier supplier = repository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(supplierDTO, supplier);
        repository.save(supplier);
    }

    public void delete(final Long id) {
        repository.deleteById(id);
    }

    private SupplierDTO mapToDTO(final Supplier supplier, final SupplierDTO supplierDTO) {
        supplierDTO.setId(supplier.getId());
        supplierDTO.setFirstName(supplier.getFirstName());
        supplierDTO.setLastName(supplier.getLastName());
        supplierDTO.setPhone(supplier.getPhone());
        supplierDTO.setEmail(supplier.getEmail());
        supplierDTO.setAddress(supplier.getAddress());
        return supplierDTO;
    }

    private Supplier mapToEntity(final SupplierDTO supplierDTO, final Supplier supplier) {
        supplier.setFirstName(supplierDTO.getFirstName());
        supplier.setLastName(supplierDTO.getLastName());
        supplier.setPhone(supplierDTO.getPhone());
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setAddress(supplierDTO.getAddress());
        return supplier;
    }

}
