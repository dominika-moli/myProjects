package ibm.java.academy.cerfiticationsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ibm.java.academy.cerfiticationsapp.model.Review;
import ibm.java.academy.cerfiticationsapp.model.User;
import ibm.java.academy.cerfiticationsapp.model.Certification;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {
    List<Review> findByCertificationAndUser(Certification certification, User user);

    List<Review> findByCertification(Certification certification);
}
