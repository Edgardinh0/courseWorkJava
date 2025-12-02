package org.zhuhsh.travelbooking.service;

import org.springframework.stereotype.Service;
import org.zhuhsh.travelbooking.model.Review;
import org.zhuhsh.travelbooking.model.Tour;
import org.zhuhsh.travelbooking.model.User;
import org.zhuhsh.travelbooking.repository.ReviewRepository;
import org.zhuhsh.travelbooking.repository.TourRepository;
import org.zhuhsh.travelbooking.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TourRepository tourRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, TourRepository tourRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.tourRepository = tourRepository;
        this.userRepository = userRepository;
    }

    public Review addReview(Long tourId, Long userId, String comment, int rating) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Tour not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Review> existing = reviewRepository.findByTourIdAndUserId(tourId, userId);
        if (existing.isPresent()) {
            throw new RuntimeException("Вы уже оставили отзыв для этого тура");
        }

        Review review = new Review(comment, rating, tour, user);
        return reviewRepository.save(review);
    }


    public List<Review> getReviewsForTour(Long tourId) {
        return reviewRepository.findByTourId(tourId);
    }

    public void deleteReview(Long reviewId, Long userId, boolean isAdmin) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!isAdmin && !review.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not allowed");
        }

        reviewRepository.delete(review);
    }
}
