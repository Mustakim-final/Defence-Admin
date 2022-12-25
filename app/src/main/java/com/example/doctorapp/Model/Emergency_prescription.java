package com.example.doctorapp.Model;

public class Emergency_prescription {
    private String name,district,subDistrict,localAddress,illness,phone,userID,myID;

    public Emergency_prescription() {
    }

    public Emergency_prescription(String name, String district, String subDistrict, String localAddress, String illness, String phone, String userID, String myID) {
        this.name = name;
        this.district = district;
        this.subDistrict = subDistrict;
        this.localAddress = localAddress;
        this.illness = illness;
        this.phone = phone;
        this.userID = userID;
        this.myID = myID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(String subDistrict) {
        this.subDistrict = subDistrict;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getIllness() {
        return illness;
    }

    public void setIllness(String illness) {
        this.illness = illness;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMyID() {
        return myID;
    }

    public void setMyID(String myID) {
        this.myID = myID;
    }
}
