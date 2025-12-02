package org.zhuhsh.travelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zhuhsh.travelbooking.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTourId(Long tourId);
    Optional<Review> findByTourIdAndUserId(Long tourId, Long userId);
}
