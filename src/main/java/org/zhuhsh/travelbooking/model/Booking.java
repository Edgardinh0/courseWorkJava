package org.zhuhsh.travelbooking.model;

import jakarta.persistence.*;

@Entity
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    private Tour tour;
    private String bookingDate;
    private String status = "PENDING";

    public void setId(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public Tour getTour() {
        return tour;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
