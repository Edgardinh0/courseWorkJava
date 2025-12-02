package org.zhuhsh.travelbooking.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zhuhsh.travelbooking.model.Booking;
import org.zhuhsh.travelbooking.model.User;
import org.zhuhsh.travelbooking.repository.UserRepository;
import org.zhuhsh.travelbooking.service.AgentBookingService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/agent/bookings")
@PreAuthorize("hasRole('AGENT')")
public class AgentBookingController {

    private final AgentBookingService agentBookingService;
    private final UserRepository userRepository;

    public AgentBookingController(AgentBookingService agentBookingService, UserRepository userRepository) {
        this.agentBookingService = agentBookingService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Booking> getAgentBookings(Principal principal) {
        String username = principal.getName();
        User agent = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return agentBookingService.getBookingsForAgent(agent.getId());
    }

    @PutMapping("/{bookingId}/status")
    public Booking updateStatus(@PathVariable Long bookingId,
                                @RequestParam String status,
                                Principal principal) {

        String username = principal.getName();
        User agent = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return agentBookingService.updateBookingStatus(bookingId, agent.getId(), status);
    }
}

