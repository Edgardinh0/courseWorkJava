package org.zhuhsh.travelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zhuhsh.travelbooking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Booking b WHERE b.tour.id = :tourId")
    void deleteAllByTourId(Long tourId);

    @Query("SELECT b FROM Booking b WHERE b.tour.agent.id = :agentId")
    List<Booking> findBookingsByAgent(Long agentId);

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.id = :bookingId AND b.tour.agent.id = :agentId")
    boolean bookingBelongsToAgent(Long bookingId, Long agentId);

    boolean existsByTourId(Long tourId);
}
