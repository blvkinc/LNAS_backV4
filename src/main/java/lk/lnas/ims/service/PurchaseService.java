package lk.lnas.ims.service;

import lk.lnas.ims.domain.Plant;
import lk.lnas.ims.domain.Purchase;
import lk.lnas.ims.domain.PurchaseItem;
import lk.lnas.ims.model.PurchaseDTO;
import lk.lnas.ims.model.PurchaseItemDTO;
import lk.lnas.ims.repos.PlantRepository;
import lk.lnas.ims.repos.PurchaseItemRepository;
import lk.lnas.ims.repos.PurchaseRepository;
import lk.lnas.ims.repos.TransactionRepository;
import lk.lnas.ims.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository repository;
    private final TransactionRepository transactionRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final PlantRepository plantRepository;

    @Transactional
    public Page<PurchaseDTO> paginate(Specification<Purchase> spec, Pageable pageable) {
        return repository.findAll(spec, pageable).map(entity -> mapToDTO(entity, new PurchaseDTO()));
    }

    public PurchaseDTO get(final Long id) {
        return repository.findById(id)
                .map(purchase -> mapToDTO(purchase, new PurchaseDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PurchaseDTO purchaseDTO) {
        final Purchase purchase = new Purchase();
        mapToEntity(purchaseDTO, purchase);
        Purchase save = repository.save(purchase);

        repository.findFirstByOrderByDocumentIndexDesc()
                .ifPresentOrElse(po -> purchase.setDocumentIndex(po.getDocumentIndex() + 1),
                        () -> purchase.setDocumentIndex(1L));

        purchase.setDocumentId(String.format("PO-%08d", purchase.getDocumentIndex()));

        purchaseDTO.getItems().forEach(item -> {
            PurchaseItem entity = mapToEntity(item, new PurchaseItem());
            entity.setPurchase(save);
            purchaseItemRepository.save(entity);
        });
        return save.getId();
    }

    public void update(final Long id, final PurchaseDTO purchaseDTO) {
        final Purchase purchase = repository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(purchaseDTO, purchase);
        repository.save(purchase);
    }

    public void delete(final Long id) {
        repository.deleteById(id);
    }

    private PurchaseDTO mapToDTO(final Purchase purchase, final PurchaseDTO purchaseDTO) {
        purchaseDTO.setId(purchase.getId());
        purchaseDTO.setStatus(purchase.getStatus());
        purchaseDTO.setType(purchase.getType());
        purchaseDTO.setSubTotal(purchase.getSubTotal());
        purchaseDTO.setDiscount(purchase.getItemDiscount());
        purchaseDTO.setTax(purchase.getTax());
        purchaseDTO.setShipping(purchase.getShipping());
        purchaseDTO.setTotal(purchase.getTotal());
        purchaseDTO.setDocumentId(purchase.getDocumentId());
        return purchaseDTO;
    }

    private void mapToEntity(final PurchaseDTO purchaseDTO, final Purchase purchase) {
        purchase.setStatus(purchaseDTO.getStatus());
        purchase.setType(purchaseDTO.getType());
        purchase.setSubTotal(purchaseDTO.getSubTotal());
        purchase.setItemDiscount(purchaseDTO.getDiscount());
        purchase.setTax(purchaseDTO.getTax());
        purchase.setShipping(purchaseDTO.getShipping());
        purchase.setTotal(purchaseDTO.getTotal());
    }

    public PurchaseItemDTO mapToDTO(final PurchaseItem purchaseItem, final PurchaseItemDTO dto) {
        dto.setId(purchaseItem.getId());
        dto.setPrice(purchaseItem.getPrice());
        dto.setDiscount(purchaseItem.getDiscount());
        dto.setQty(purchaseItem.getQty());
        dto.setPlant(purchaseItem.getPlant().getId());
        dto.setDescription(purchaseItem.getDescription());
        return dto;
    }

    public PurchaseItem mapToEntity(final PurchaseItemDTO purchaseItemDTO, final PurchaseItem entity) {
        entity.setId(purchaseItemDTO.getId());
        entity.setPrice(purchaseItemDTO.getPrice());
        entity.setDiscount(purchaseItemDTO.getDiscount());
        entity.setQty(purchaseItemDTO.getQty());

        final Plant plant = plantRepository.findById(purchaseItemDTO.getPlant())
                .orElseThrow(() -> new NotFoundException("plant not found"));

        entity.setPlant(plant);
        entity.setDescription(purchaseItemDTO.getDescription());
        return entity;
    }


}
