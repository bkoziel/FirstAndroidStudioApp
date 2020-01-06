package com.example.wastic.Activity;

public class Comments {
    public String description;
    public  String imageUser;
    public String username;
    public double rating;
    public String date;

    public Comments() {

    }

    public Comments(String description, String imageUser, String username, double rating, String date) {
        this.description = description;
        this.imageUser = imageUser;
        this.username = username;
        this.rating = rating;
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUser() {
        return imageUser;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
