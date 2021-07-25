package ibm.java.academy.cerfiticationsapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ibm.java.academy.cerfiticationsapp.model.ConfirmationToken;

public interface ConfirmationTokenJpaRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findConfirmationTokenByConfirmationToken(String token);
}
