package com.example.ligapramim;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Contact implements Serializable {

    private String name;
    private Bitmap photo;
    private int phoneNumber;

    public Contact(String name, Bitmap photo, int phoneNumber ){
        this.name = name;
        this.photo = photo;
        this.phoneNumber = phoneNumber;
    }


    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
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
}
