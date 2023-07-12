package lk.lnas.ims.repos;

import lk.lnas.ims.domain.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SalaryRepository extends JpaRepository<Salary, Long>, JpaSpecificationExecutor<Salary> {
}
