package com.example.ligapramim;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Contact implements Serializable {

    private String name;
    private Bitmap photo;
    private String phoneNumber;
    private String observation;
    private int id;

    public Contact(){
    }

    public Contact(String name, Bitmap photo, String phoneNumber, String observation ){
        this.name = name;
        this.photo = photo;
        this.phoneNumber = phoneNumber;
        this.observation = observation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObservation() { return observation; }

    public void setObservation(String observation) { this.observation = observation; }
}
