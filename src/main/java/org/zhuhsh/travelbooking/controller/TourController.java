package org.zhuhsh.travelbooking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zhuhsh.travelbooking.model.Tour;
import org.zhuhsh.travelbooking.model.User;
import org.zhuhsh.travelbooking.repository.UserRepository;
import org.zhuhsh.travelbooking.service.TourService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TourController {
    private final TourService tourService;
    private final UserRepository userRepository;

    public TourController(TourService tourService, UserRepository userRepository) {
        this.tourService = tourService;
        this.userRepository = userRepository;
    }

    @GetMapping("/tours")
    public List<Tour> getAllTours() {
        return tourService.getAllTours();
    }

    @PostMapping("/agent/tours")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    public Tour addTour(@RequestBody Tour tour, Principal principal) {


        User agent = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        tour.setAgent(agent); // привязываем тур к агенту

        return tourService.addTour(tour);
    }


    @GetMapping("/tours/filter")
    public List<Tour> filterTours(@RequestParam(required = false) String startDate,
                                  @RequestParam(required = false) String endDate,
                                  @RequestParam(required = false) Double minPrice,
                                  @RequestParam(required = false) Double maxPrice) {
        return tourService.filterTours(startDate, endDate, minPrice, maxPrice);
    }

    @DeleteMapping("/tours/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','AGENT')")
    public ResponseEntity<String> deleteTour(@PathVariable Long id) {
        tourService.deleteTour(id);
        return ResponseEntity.ok("Tour deleted successfully");
    }
}
