package org.zhuhsh.travelbooking.service;

import org.springframework.stereotype.Service;
import org.zhuhsh.travelbooking.model.Tour;
import org.zhuhsh.travelbooking.repository.BookingRepository;
import org.zhuhsh.travelbooking.repository.TourRepository;

import java.util.List;

@Service
public class TourService {
    private final TourRepository tourRepository;
    private final BookingRepository bookingRepository;

    public TourService(TourRepository tourRepository, BookingRepository bookingRepository) {
        this.tourRepository = tourRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Tour addTour(Tour tour) {
        return tourRepository.save(tour);
    }

    public List<Tour> filterTours(String startDate, String endDate, Double minPrice, Double maxPrice) {
        return tourRepository.findAll().stream().filter(t ->
                (startDate == null || t.getStartDate().compareTo(startDate) >= 0) &&
                        (endDate == null || t.getEndDate().compareTo(endDate) <= 0) &&
                        (minPrice == null || t.getPrice() >= minPrice) &&
                        (maxPrice == null || t.getPrice() <= maxPrice)
        ).toList();
    }

    public void deleteTour(Long id) {
        bookingRepository.deleteAllByTourId(id);
        tourRepository.deleteById(id);
    }
}
