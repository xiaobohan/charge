package com.starrypay.bean;

import java.io.Serializable;

public class ContactBean implements Serializable {

    public String name;

    public String phone;

    public ContactBean(String phone, String name) {
        this.name = name;
        this.phone = phone;
    }
}
