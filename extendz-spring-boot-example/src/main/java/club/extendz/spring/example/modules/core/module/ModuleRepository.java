package club.extendz.spring.example.modules.core.module;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends PagingAndSortingRepository<Module, Long> {

}
