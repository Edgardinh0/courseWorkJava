package org.zhuhsh.travelbooking.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zhuhsh.travelbooking.model.Review;
import org.zhuhsh.travelbooking.model.User;
import org.zhuhsh.travelbooking.repository.UserRepository;
import org.zhuhsh.travelbooking.service.ReviewService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;

    public ReviewController(ReviewService reviewService, UserRepository userRepository) {
        this.reviewService = reviewService;
        this.userRepository = userRepository;
    }

    // Оставить отзыв
    @PostMapping("/tour/{tourId}")
    @PreAuthorize("hasRole('TRAVELER')")
    public Review addReview(@PathVariable Long tourId,
                            @RequestParam String comment,
                            @RequestParam int rating,
                            Principal principal) {

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return reviewService.addReview(tourId, user.getId(), comment, rating);
    }


    // Получить отзывы для тура
    @GetMapping("/tour/{tourId}")
    public List<Review> getReviews(@PathVariable Long tourId) {
        return reviewService.getReviewsForTour(tourId);
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('TRAVELER') or hasRole('ADMIN')")
    public void deleteReview(@PathVariable Long reviewId, Principal principal) {
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAdmin = user.getRole().equals("ADMIN");

        reviewService.deleteReview(reviewId, user.getId(), isAdmin);
    }


}
