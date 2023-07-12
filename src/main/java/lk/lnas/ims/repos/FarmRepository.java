package lk.lnas.ims.repos;

import lk.lnas.ims.domain.Farm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface FarmRepository extends JpaRepository<Farm, Long>, JpaSpecificationExecutor<Farm> {

    boolean existsByNameIgnoreCase(String name);

}
