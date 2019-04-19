package com.example.contentproviderdemo1.model;

import java.io.Serializable;

public class Customer implements Serializable {

    public int id;
    public String docId;
    public String name;
    public String phone;
    public String email;

    public Customer(){

    }

    public Customer(int id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Customer(int id, String docId, String name, String phone, String email) {
        this.id = id;
        this.docId = docId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
    public String toString() {
        String message = "Name: "+name+"\nPhone: "+phone+"\nEmail: "+email;
        return message;
    }
}
