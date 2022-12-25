package com.example.doctorapp.Model;

public class Diagnostic {

    private String id,title,address,phone,item,price,diagnostic;

    public Diagnostic() {
    }

    public Diagnostic(String id, String title, String address, String phone, String item, String price, String diagnostic) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.item = item;
        this.price = price;
        this.diagnostic = diagnostic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }
}
