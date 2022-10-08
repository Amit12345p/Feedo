package com.example.feedo;


import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String donarName,phoneNumber,address,donationType,cookedTime,placeType,type;

    public User() {
    }

    public User(String donarName, String phoneNumber, String address, String donationType, String cookedTime, String placeType, String type) {
        this.donarName = donarName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.donationType = donationType;
        this.cookedTime = cookedTime;
        this.placeType = placeType;
        this.type = type;
    }

    public String getDonarName() {
        return donarName;
    }

    public void setDonarName(String donarName) {
        this.donarName = donarName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDonationType() {
        return donationType;
    }

    public void setDonationType(String donationType) {
        this.donationType = donationType;
    }

    public String getCookedTime() {
        return cookedTime;
    }

    public void setCookedTime(String cookedTime) {
        this.cookedTime = cookedTime;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
