package org.zhuhsh.travelbooking.controller;

import org.springframework.web.bind.annotation.RestController;
import org.zhuhsh.travelbooking.model.Tour;
import org.zhuhsh.travelbooking.service.TourService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TourController {
    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping("/tours")
    public List<Tour> getAllTours() {
        return tourService.getAllTours();
    }

    @PostMapping("/agent/tours")
    public Tour addTour(@RequestBody Tour tour) {
        return tourService.addTour(tour);
    }

    @GetMapping("/tours/filter")
    public List<Tour> filterTours(@RequestParam(required = false) String startDate,
                                  @RequestParam(required = false) String endDate,
                                  @RequestParam(required = false) Double minPrice,
                                  @RequestParam(required = false) Double maxPrice) {
        return tourService.filterTours(startDate, endDate, minPrice, maxPrice);
    }
}
