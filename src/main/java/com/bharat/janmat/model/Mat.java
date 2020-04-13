package com.bharat.janmat.model;

import java.time.ZonedDateTime;

public class Mat {

    private Long id;
    private String city;
    private String about;
    private String details;
    private ZonedDateTime postedOn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public ZonedDateTime getPostedOn() {
        return postedOn;
    }

    public void setPostedOn(ZonedDateTime postedOn) {
        this.postedOn = postedOn;
    }
}