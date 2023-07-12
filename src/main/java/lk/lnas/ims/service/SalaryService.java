package lk.lnas.ims.service;

import lk.lnas.ims.domain.Employee;
import lk.lnas.ims.domain.Salary;
import lk.lnas.ims.model.SalaryDTO;
import lk.lnas.ims.repos.EmployeeRepository;
import lk.lnas.ims.repos.SalaryRepository;
import lk.lnas.ims.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SalaryService {

    private final SalaryRepository repository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public Page<SalaryDTO> paginate(Specification<Salary> spec, Pageable pageable) {
        return repository.findAll(spec, pageable).map(entity -> mapToDTO(entity, new SalaryDTO()));
    }

    public SalaryDTO get(final Long id) {
        return repository.findById(id).map(salary -> mapToDTO(salary, new SalaryDTO())).orElseThrow(NotFoundException::new);
    }

    public Long create(final SalaryDTO salaryDTO) {
        final Salary salary = new Salary();
        mapToEntity(salaryDTO, salary);
        return repository.save(salary).getId();
    }

    public void update(final Long id, final SalaryDTO salaryDTO) {
        final Salary salary = repository.findById(id).orElseThrow(NotFoundException::new);
        mapToEntity(salaryDTO, salary);
        repository.save(salary);
    }

    public void delete(final Long id) {
        repository.deleteById(id);
    }

    private SalaryDTO mapToDTO(final Salary salary, final SalaryDTO salaryDTO) {
        salaryDTO.setId(salary.getId());
        salaryDTO.setAmount(salary.getAmount());
        salaryDTO.setPaidOn(salary.getPaidOn());
        salaryDTO.setDescription(salary.getDescription());
        salaryDTO.setStatus(salary.getStatus());
        salaryDTO.setEmployee(salary.getEmployee() == null ? null : salary.getEmployee().getId());
        return salaryDTO;
    }

    private Salary mapToEntity(final SalaryDTO salaryDTO, final Salary salary) {
        salary.setAmount(salaryDTO.getAmount());
        salary.setPaidOn(salaryDTO.getPaidOn());
        salary.setDescription(salaryDTO.getDescription());
        salary.setStatus(salaryDTO.getStatus());
        final Employee employee = salaryDTO.getEmployee() == null ? null : employeeRepository.findById(salaryDTO.getEmployee()).orElseThrow(() -> new NotFoundException("employee not found"));
        salary.setEmployee(employee);
        return salary;
    }

}
