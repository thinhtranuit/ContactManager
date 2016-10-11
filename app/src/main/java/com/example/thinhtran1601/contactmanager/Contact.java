package com.example.thinhtran1601.contactmanager;

import java.io.Serializable;

/**
 * Created by thinh on 02/10/2016.
 */
public class Contact implements Serializable {
    private String name;
    private String phoneNumber;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
