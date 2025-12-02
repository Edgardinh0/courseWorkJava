package org.zhuhsh.travelbooking.service;

import org.springframework.stereotype.Service;
import org.zhuhsh.travelbooking.model.Booking;
import org.zhuhsh.travelbooking.repository.BookingRepository;
import org.zhuhsh.travelbooking.repository.UserRepository;

import java.util.List;

@Service
public class AgentBookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public AgentBookingService(BookingRepository bookingRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    public List<Booking> getBookingsForAgent(Long agentId) {
        return bookingRepository.findBookingsByAgent(agentId);
    }

    public Booking updateBookingStatus(Long bookingId, Long agentId, String status) {
        if (!bookingRepository.bookingBelongsToAgent(bookingId, agentId)) {
            throw new RuntimeException("Access denied");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus(status);
        return bookingRepository.save(booking);
    }
}

