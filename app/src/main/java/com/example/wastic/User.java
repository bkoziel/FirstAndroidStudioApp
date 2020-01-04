package com.example.wastic;
public class User {

    private int id;
    private String username, email, gender, since, avatar;

    public User(int id, String username, String email, String gender, String since, String avatar) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.since = since;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getSince() {
        return since;
    }

    public String getAvatar() {
        return avatar;
    }
}