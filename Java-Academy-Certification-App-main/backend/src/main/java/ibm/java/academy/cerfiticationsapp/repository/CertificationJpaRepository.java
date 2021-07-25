package ibm.java.academy.cerfiticationsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ibm.java.academy.cerfiticationsapp.model.Certification;

public interface CertificationJpaRepository extends JpaRepository<Certification, Long>{

}
