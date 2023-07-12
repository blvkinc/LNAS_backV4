package lk.lnas.ims.service;

import lk.lnas.ims.domain.Farm;
import lk.lnas.ims.model.FarmDTO;
import lk.lnas.ims.repos.FarmRepository;
import lk.lnas.ims.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class FarmService {

    private final FarmRepository repository;

    @Transactional
    public Page<FarmDTO> paginate(Specification<Farm> spec, Pageable pageable) {
        return repository.findAll(spec, pageable).map(entity -> mapToDTO(entity, new FarmDTO()));
    }

    public FarmDTO get(final Long id) {
        return repository.findById(id)
                .map(farm -> mapToDTO(farm, new FarmDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final FarmDTO farmDTO) {
        final Farm farm = new Farm();
        mapToEntity(farmDTO, farm);
        return repository.save(farm).getId();
    }

    public void update(final Long id, final FarmDTO farmDTO) {
        final Farm farm = repository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(farmDTO, farm);
        repository.save(farm);
    }

    public void delete(final Long id) {
        repository.deleteById(id);
    }

    private FarmDTO mapToDTO(final Farm farm, final FarmDTO farmDTO) {
        farmDTO.setId(farm.getId());
        farmDTO.setName(farm.getName());
        farmDTO.setLocation(farm.getLocation());
        farmDTO.setDescription(farm.getDescription());
        farmDTO.setStatus(farm.getStatus());
        return farmDTO;
    }

    private Farm mapToEntity(final FarmDTO farmDTO, final Farm farm) {
        farm.setName(farmDTO.getName());
        farm.setLocation(farmDTO.getLocation());
        farm.setDescription(farmDTO.getDescription());
        farm.setStatus(farmDTO.getStatus());
        return farm;
    }

    public boolean nameExists(final String name) {
        return repository.existsByNameIgnoreCase(name);
    }

}
