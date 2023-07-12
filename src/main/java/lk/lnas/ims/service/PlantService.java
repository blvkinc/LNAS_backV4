package lk.lnas.ims.service;

import lk.lnas.ims.domain.Plant;
import lk.lnas.ims.model.PlantDTO;
import lk.lnas.ims.repos.PlantRepository;
import lk.lnas.ims.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository repository;

    @Transactional
    public Page<PlantDTO> paginate(Specification<Plant> spec, Pageable pageable) {
        return repository.findAll(spec, pageable).map(entity -> mapToDTO(entity, new PlantDTO()));
    }

    public PlantDTO get(final Long id) {
        return repository.findById(id)
                .map(plant -> mapToDTO(plant, new PlantDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PlantDTO plantDTO) {
        final Plant plant = new Plant();
        mapToEntity(plantDTO, plant);
        return repository.save(plant).getId();
    }

    public void update(final Long id, final PlantDTO plantDTO) {
        final Plant plant = repository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(plantDTO, plant);
        repository.save(plant);
    }

    public void delete(final Long id) {
        repository.deleteById(id);
    }

    private PlantDTO mapToDTO(final Plant plant, final PlantDTO plantDTO) {
        plantDTO.setId(plant.getId());
        plantDTO.setName(plant.getName());
        plantDTO.setProductId(plant.getProductId());
        plantDTO.setSalesPrice(plant.getSalesPrice());
        plantDTO.setPurchasePrice(plant.getPurchasePrice());
        plantDTO.setQtyAtHand(plant.getQtyAtHand());
        plantDTO.setQtyPotential(plant.getQtyPotential());
        plantDTO.setScientificName(plant.getScientificName());
        plantDTO.setDescription(plant.getDescription());
        plantDTO.setStatus(plant.getStatus());
        return plantDTO;
    }

    private Plant mapToEntity(final PlantDTO plantDTO, final Plant plant) {
        plant.setName(plantDTO.getName());
        plant.setProductId(plantDTO.getProductId());
        plant.setSalesPrice(plantDTO.getSalesPrice());
        plant.setPurchasePrice(plantDTO.getPurchasePrice());
        plant.setQtyAtHand(plantDTO.getQtyAtHand());
        plant.setQtyPotential(plantDTO.getQtyPotential());
        plant.setScientificName(plantDTO.getScientificName());
        plant.setDescription(plantDTO.getDescription());
        plant.setStatus(plantDTO.getStatus());
        return plant;
    }

}
