package org.zhuhsh.travelbooking.model;

import jakarta.persistence.*;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;
    private int rating;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Review() {}

    public Review(String comment, int rating, Tour tour, User user) {
        this.comment = comment;
        this.rating = rating;
        this.tour = tour;
        this.user = user;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public String getComment() {
        return comment;
    }

    public User getUser() {
        return user;
    }

    public Tour getTour() {
        return tour;
    }

    public int getRating() {
        return rating;
    }

    public Long getId() {
        return id;
    }
}
