package lk.lnas.ims.service;

import lk.lnas.ims.domain.Transaction;
import lk.lnas.ims.model.TransactionDTO;
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
public class TransactionService {

    private final TransactionRepository repository;

    @Transactional
    public Page<TransactionDTO> paginate(Specification<Transaction> spec, Pageable pageable) {
        return repository.findAll(spec, pageable).map(entity -> mapToDTO(entity, new TransactionDTO()));
    }

    public TransactionDTO get(final Long id) {
        return repository.findById(id)
                .map(transaction -> mapToDTO(transaction, new TransactionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TransactionDTO transactionDTO) {
        final Transaction transaction = new Transaction();
        mapToEntity(transactionDTO, transaction);
        return repository.save(transaction).getId();
    }

    public void update(final Long id, final TransactionDTO transactionDTO) {
        final Transaction transaction = repository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(transactionDTO, transaction);
        repository.save(transaction);
    }

    public void delete(final Long id) {
        repository.deleteById(id);
    }

    private TransactionDTO mapToDTO(final Transaction transaction,
                                    final TransactionDTO transactionDTO) {
        transactionDTO.setId(transaction.getId());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setDate(transaction.getDate());
        transactionDTO.setMethod(transaction.getMethod());
        return transactionDTO;
    }

    private Transaction mapToEntity(final TransactionDTO transactionDTO,
                                    final Transaction transaction) {
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDate(transactionDTO.getDate());
        transaction.setMethod(transactionDTO.getMethod());
        return transaction;
    }

}
