package org.zhuhsh.travelbooking.controller;

import org.zhuhsh.travelbooking.model.Booking;
import org.zhuhsh.travelbooking.model.User;
import org.zhuhsh.travelbooking.service.BookingService;
import org.zhuhsh.travelbooking.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/traveler")
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;

    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @PostMapping("/booking")
    public Booking createBooking(@RequestParam Long userId, @RequestParam Long tourId) {
        User user = userService.getUserById(userId);
        return bookingService.createBooking(user, tourId);
    }

    @GetMapping("/bookings")
    public List<Booking> getUserBookings(@RequestParam Long userId) {
        return bookingService.getBookingsByUser(userId);
    }

    @DeleteMapping("/booking/{bookingId}")
    public void cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
    }
}

