package org.zhuhsh.travelbooking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.zhuhsh.travelbooking.model.Tour;
import org.zhuhsh.travelbooking.repository.BookingRepository;
import org.zhuhsh.travelbooking.repository.TourRepository;
import org.zhuhsh.travelbooking.service.TourService;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

class TourServiceTest {

    private final TourRepository tourRepository = mock(TourRepository.class);
    private final BookingRepository bookingRepository = mock(BookingRepository.class);

    private final TourService tourService = new TourService(tourRepository, bookingRepository);

    @Test
    void testAddTourRejectsNegativePrice() {
        Tour tour = new Tour();
        tour.setName("Test Tour");
        tour.setDestination("Paris");
        tour.setStartDate(LocalDate.now().plusDays(5).toString());
        tour.setEndDate(LocalDate.now().plusDays(10).toString());
        tour.setPrice(-100);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            tourService.addTour(tour);
        });
    }

    @Test
    void testAddTourRejectsStartAfterEnd() {
        Tour tour = new Tour();
        tour.setName("Test Tour");
        tour.setDestination("Paris");
        tour.setStartDate(LocalDate.now().plusDays(10).toString());
        tour.setEndDate(LocalDate.now().plusDays(5).toString());
        tour.setPrice(500);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            tourService.addTour(tour);
        });
    }
}

