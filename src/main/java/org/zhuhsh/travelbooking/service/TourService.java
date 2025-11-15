package org.zhuhsh.travelbooking.service;

import org.zhuhsh.travelbooking.model.Tour;
import org.springframework.stereotype.Service;
import org.zhuhsh.travelbooking.repository.TourRepository;

import java.util.List;

@Service
public class TourService {
    private final TourRepository tourRepository;

    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Tour addTour(Tour tour) {
        return tourRepository.save(tour);
    }

    public List<Tour> filterTours(String startDate, String endDate, Double minPrice, Double maxPrice){
        return tourRepository.findAll().stream().filter(t -> (startDate == null || t.getStartDate().compareTo(startDate) >= 0) &&
                (endDate == null || t.getEndDate().compareTo(endDate) <= 0) &&
                (minPrice == null || t.getPrice() >= minPrice) &&
                (maxPrice == null || t.getPrice() <= maxPrice)).toList();
    }
}
