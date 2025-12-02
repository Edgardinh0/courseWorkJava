package org.zhuhsh.travelbooking.service;

import org.zhuhsh.travelbooking.model.Tour;
import org.zhuhsh.travelbooking.model.Booking;
import org.zhuhsh.travelbooking.model.User;
import org.zhuhsh.travelbooking.repository.BookingRepository;
import org.zhuhsh.travelbooking.repository.TourRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final TourRepository tourRepository;

    public BookingService(BookingRepository bookingRepository, TourRepository tourRepository) {
        this.bookingRepository = bookingRepository;
        this.tourRepository = tourRepository;
    }

    public Booking createBooking(User user, Long tourId) {
        Tour tour = tourRepository.findById(tourId).orElseThrow();
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTour(tour);
        booking.setBookingDate(LocalDate.now().toString());
        booking.setStatus("PENDING");
        return bookingRepository.save(booking);
    }


    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        bookingRepository.delete(booking);
    }
}

