package ibm.java.academy.cerfiticationsapp.repository;

import ibm.java.academy.cerfiticationsapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<Role, String> {
}
