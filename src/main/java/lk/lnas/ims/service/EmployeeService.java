package lk.lnas.ims.service;

import lk.lnas.ims.domain.Employee;
import lk.lnas.ims.domain.Farm;
import lk.lnas.ims.model.EmployeeDTO;
import lk.lnas.ims.repos.EmployeeRepository;
import lk.lnas.ims.repos.FarmRepository;
import lk.lnas.ims.util.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;


@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final FarmRepository farmRepository;

    @Transactional
    public Page<EmployeeDTO> paginate(Specification<Employee> spec, Pageable pageable) {
        return repository.findAll(spec, pageable).map(entity -> mapToDTO(entity, new EmployeeDTO()));
    }

    public EmployeeDTO get(final Long id) {
        return repository.findById(id)
                .map(employee -> mapToDTO(employee, new EmployeeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final EmployeeDTO employeeDTO) {
        final Employee employee = new Employee();
        mapToEntity(employeeDTO, employee);
        return repository.save(employee).getId();
    }

    public void update(final Long id, final EmployeeDTO employeeDTO) {
        final Employee employee = repository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(employeeDTO, employee);
        repository.save(employee);
    }

    public void delete(final Long id) {
        repository.deleteById(id);
    }

    private EmployeeDTO mapToDTO(final Employee employee, final EmployeeDTO employeeDTO) {
        employeeDTO.setId(employee.getId());
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setPhone(employee.getPhone());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setAddress(employee.getAddress());
        employeeDTO.setStatus(employee.getStatus());
        employeeDTO.setFarms(employee.getFarms() == null ? null : employee.getFarms().stream()
                .map(Farm::getId)
                .toList());
        return employeeDTO;
    }

    private Employee mapToEntity(final EmployeeDTO employeeDTO, final Employee employee) {
        employee.setStatus(employeeDTO.getStatus());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setPhone(employeeDTO.getPhone());
        employee.setEmail(employeeDTO.getEmail());
        employee.setAddress(employeeDTO.getAddress());
        final List<Farm> farms = farmRepository.findAllById(
                employeeDTO.getFarms() == null ? Collections.emptyList() : employeeDTO.getFarms());
        if (farms.size() != (employeeDTO.getFarms() == null ? 0 : employeeDTO.getFarms().size())) {
            throw new NotFoundException("one of farms not found");
        }
        employee.setFarms(new HashSet<>(farms));
        return employee;
    }

}
