package com.example.doctorapp.Model;

public class Pharmacy {

    private String id,title,address,phone,pharmacy,name,image,note,new_id,prescription,pharmacy_address,pharmacy_title,pharmacy_phone,pharmacy_id;
    String key;
    public Pharmacy() {
    }


    public Pharmacy(String id, String title, String address, String phone, String pharmacy, String name, String image, String note, String new_id, String prescription, String pharmacy_address, String pharmacy_title, String pharmacy_phone, String pharmacy_id) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.phone = phone;
        this.pharmacy = pharmacy;
        this.name = name;
        this.image = image;
        this.note = note;
        this.new_id = new_id;
        this.prescription = prescription;
        this.pharmacy_address = pharmacy_address;
        this.pharmacy_title = pharmacy_title;
        this.pharmacy_phone = pharmacy_phone;
        this.pharmacy_id = pharmacy_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(String pharmacy) {
        this.pharmacy = pharmacy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNew_id() {
        return new_id;
    }

    public void setNew_id(String new_id) {
        this.new_id = new_id;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getPharmacy_address() {
        return pharmacy_address;
    }

    public void setPharmacy_address(String pharmacy_address) {
        this.pharmacy_address = pharmacy_address;
    }

    public String getPharmacy_title() {
        return pharmacy_title;
    }

    public void setPharmacy_title(String pharmacy_title) {
        this.pharmacy_title = pharmacy_title;
    }

    public String getPharmacy_phone() {
        return pharmacy_phone;
    }

    public void setPharmacy_phone(String pharmacy_phone) {
        this.pharmacy_phone = pharmacy_phone;
    }

    public String getPharmacy_id() {
        return pharmacy_id;
    }

    public void setPharmacy_id(String pharmacy_id) {
        this.pharmacy_id = pharmacy_id;
    }
}
