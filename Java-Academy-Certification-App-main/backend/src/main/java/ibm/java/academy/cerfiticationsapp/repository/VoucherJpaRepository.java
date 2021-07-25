package ibm.java.academy.cerfiticationsapp.repository;


import java.util.Optional;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import ibm.java.academy.cerfiticationsapp.model.Voucher;

public interface VoucherJpaRepository extends JpaRepository<Voucher, Long> {

    Optional<Voucher> findById(Long id);

    List<Voucher> findAll();
    

}
