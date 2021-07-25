package ibm.java.academy.cerfiticationsapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ibm.java.academy.cerfiticationsapp.model.Review;
import ibm.java.academy.cerfiticationsapp.service.ReviewService;

@Controller
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping(path = "/add-review/{userId}/{certId}", produces = "application/json")
    @ResponseBody
    public Review addReview(@PathVariable("userId") Long userId, @PathVariable("certId") Long certId,
            @RequestParam(name = "comment") String comment, @RequestParam(name = "rating") Integer rating) {
        return reviewService.createReviewParams(userId, certId, comment, rating);
    }

    @GetMapping(path = "/reviews/{certId}")
    @ResponseBody
    public List<Review> getReviews(@PathVariable(name = "certId") Long certId) {
        List<Review> reviews = reviewService.findReviewsByCert(certId);
        return reviews;
    }

    @GetMapping(path = "/certReview/{certId}")
    @ResponseBody
    public Double getAvgRating(@PathVariable(name = "certId") Long certId) {
        List<Review> reviews = reviewService.findReviewsByCert(certId);
        Double average = reviews.stream().mapToInt(r -> r.getRating()).average().orElse(0);
        return average;
    }
}
