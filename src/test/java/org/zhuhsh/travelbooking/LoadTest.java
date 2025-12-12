package org.zhuhsh.travelbooking;

import org.junit.jupiter.api.Test;
import org.zhuhsh.travelbooking.service.TourService;
import org.zhuhsh.travelbooking.repository.TourRepository;
import org.zhuhsh.travelbooking.repository.BookingRepository;

import static org.mockito.Mockito.mock;

class LoadTest {

    @Test
    void stressTestTourFiltering() {
        TourService service = new TourService(
                mock(TourRepository.class),
                mock(BookingRepository.class)
        );

        for (int i = 0; i < 10_000; i++) {
            service.filterTours(null, null, null, null);
        }
    }
}

