package com.example.ez_rental.Model;

import java.io.Serializable;

public class User  implements Serializable {
    private String User_ID;
    private String Username ;
    private String User_Email;

    private String User_ContactNo ;
    private String Address;
    private String User_Password;
    private String User_Profile;
    private String Driver_license;
    private String License_ExpiryDate;

    public User(String user_ID, String username, String user_Email, String user_ContactNo, String address, String user_Password, String user_Profile, String driver_license, String license_ExpiryDate) {
        User_ID = user_ID;
        Username = username;
        User_Email = user_Email;
        User_Password = user_Password;
        User_ContactNo = user_ContactNo;
        Address = address;
        User_Profile = user_Profile;
        Driver_license = driver_license;
        License_ExpiryDate = license_ExpiryDate;
    }


    @Override
    public String toString() {
        return "User{" +
                "User_ID='" + User_ID + '\'' +
                ", Username='" + Username + '\'' +
                ", User_Email='" + User_Email + '\'' +
                ", User_Password='" + User_Password + '\'' +
                ", User_ContactNo='" + User_ContactNo + '\'' +
                ", Address='" + Address + '\'' +
                ", User_Profile='" + User_Profile + '\'' +
                ", Driver_license='" + Driver_license + '\'' +
                ", License_ExpiryDate='" + License_ExpiryDate + '\'' +
                '}';
    }

    public String getLicense_ExpiryDate() {
        return License_ExpiryDate;
    }

    public void setLicense_ExpiryDate(String license_ExpiryDate) {
        License_ExpiryDate = license_ExpiryDate;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUser_Email() {
        return User_Email;
    }

    public void setUser_Email(String user_Email) {
        User_Email = user_Email;
    }

    public String getUser_Password() {
        return User_Password;
    }

    public void setUser_Password(String user_Password) {
        User_Password = user_Password;
    }

    public String getUser_ContactNo() {
        return User_ContactNo;
    }

    public void setUser_ContactNo(String user_ContactNo) {
        User_ContactNo = user_ContactNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getUser_Profile() {
        return User_Profile;
    }

    public void setUser_Profile(String user_profile) {
        User_Profile = user_profile;
    }

    public String getDriver_license() {
        return Driver_license;
    }

    public void setDriver_license(String driver_license) {
        Driver_license = driver_license;
    }
}
