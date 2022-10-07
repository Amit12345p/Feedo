package com.example.feedo;


import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String donarname,phoneNo,address,donationType,cookedBefore,chooseTypeOfPlace,status;

//    public User(String donarname, String phoneNo, String address, String donationType, String cookedBefore, String chooseTypeOfPlace, String status) {
//        this.donarname = donarname;
//        this.phoneNo = phoneNo;
//        this.address = address;
//        this.donationType = donationType;
//        this.cookedBefore = cookedBefore;
//        this.chooseTypeOfPlace = chooseTypeOfPlace;
//        this.status = status;
//    }
//
//    public User() {
//    }

    public String getDonarname() {
        return donarname;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public String getDonationType() {
        return donationType;
    }

    public String getCookedBefore() {
        return cookedBefore;
    }

    public String getChooseTypeOfPlace() {
        return chooseTypeOfPlace;
    }

    public String getStatus() {
        return status;
    }
}
