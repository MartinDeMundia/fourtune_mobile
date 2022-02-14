package com.coreictconsultancy.fourtune.pojo;

import java.io.Serializable;


public class Res_Event_Details implements Serializable {
    String id, event_name, event_date, event_description, price,event_featured_image,event_location ;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getEventname() {
        return event_name;
    }
    public void setEventname(String event_name) {
        this.event_name = event_name;
    }

    public String getEventdate() {
        return event_date;
    }
    public void setEventdate(String event_date) {
        this.event_date = event_date;
    }

    public String getEventdescription() {
        return event_description;
    }
    public void setEventdescription(String event_description) {
        this.event_description = event_description;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getEventfeatured_image() {
        return event_featured_image;
    }
    public void setEventfeatured_image(String event_featured_image) {
        this.event_featured_image = event_featured_image;
    }
    public String getEventlocation() {
        return event_location;
    }
    public void setEventlocation(String event_location) {
        this.event_location = event_location;
    }
}

