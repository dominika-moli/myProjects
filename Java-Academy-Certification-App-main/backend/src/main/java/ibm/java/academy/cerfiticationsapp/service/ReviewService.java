package ibm.java.academy.cerfiticationsapp.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibm.java.academy.cerfiticationsapp.model.Certification;
import ibm.java.academy.cerfiticationsapp.model.Review;
import ibm.java.academy.cerfiticationsapp.model.User;
import ibm.java.academy.cerfiticationsapp.repository.CertificationJpaRepository;
import ibm.java.academy.cerfiticationsapp.repository.ReviewJpaRepository;
import ibm.java.academy.cerfiticationsapp.repository.UserJpaRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewJpaRepository reviewRepo;

    @Autowired
    private CertificationJpaRepository certRepo;

    @Autowired
    private CertificationService cService;

    @Autowired
    private UserJpaRepository userRepo;

    public Review createReviewParams(Long userId, Long certId, String comment, Integer rating) {

        Optional<Certification> certOpt = certRepo.findById(certId);
        Optional<User> userOpt = userRepo.findById(userId);

        try {
            List<Review> reviews = reviewRepo.findByCertificationAndUser(certOpt.get(), userOpt.get());
            if (reviews.size() > 0) {
                throw new IllegalArgumentException("You already made review for this certification.");
            }
        } catch (NoSuchElementException e) {
        }

        Review result = null;

        if (certOpt.isPresent() && userOpt.isPresent()) {
            Review review = new Review();
            review.setCertification(certOpt.get());
            review.setUser(userOpt.get());
            review.setComment(comment);
            review.setRating(rating);
            review.setPosted(new Date());

            result = reviewRepo.save(review);
        }

        return result;
    }

    public List<Review> findReviewsByCert(Long certId) {
        Optional<Certification> certOpt = certRepo.findById(certId);
        List<Review> reviews = reviewRepo.findByCertification(certOpt.get());

        return reviews;
    }

    public void deleteReview(Long id) {
        reviewRepo.deleteById(id);
    }

}
