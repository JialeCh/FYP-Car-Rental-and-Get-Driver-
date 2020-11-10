package com.example.ez_rental.Model;

import java.io.Serializable;

public class Car_Driver implements Serializable {
    private int CD_ID;
    private String CD_Name;
    private String CD_BOD;
    private String gender;
    private String phone_no;
    private String language_speak;
    private int CD_LicenseNo;
    private String CD_License_ExpiryDate;
    private String CD_Status;
    private String reason;
    private int Admin_Id;


    public Car_Driver() {
    }

    public Car_Driver(int CD_ID, String CD_Name, String CD_BOD, String gender, String phone_no, String language_speak, int CD_LicenseNo, String CD_License_ExpiryDate, String CD_Status, String reason, int admin_Id) {
        this.CD_ID = CD_ID;
        this.CD_Name = CD_Name;
        this.CD_BOD = CD_BOD;
        this.gender = gender;
        this.phone_no = phone_no;
        this.language_speak = language_speak;
        this.CD_LicenseNo = CD_LicenseNo;
        this.CD_License_ExpiryDate = CD_License_ExpiryDate;
        this.CD_Status = CD_Status;
        this.reason = reason;
        Admin_Id = admin_Id;
    }

    @Override
    public String toString() {
        return "Car_Driver{" +
                "CD_ID=" + CD_ID +
                ", CD_Name='" + CD_Name + '\'' +
                ", CD_BOD='" + CD_BOD + '\'' +
                ", gender='" + gender + '\'' +
                ", phone_no='" + phone_no + '\'' +
                ", language_speak='" + language_speak + '\'' +
                ", CD_LicenseNo=" + CD_LicenseNo +
                ", CD_License_ExpiryDate='" + CD_License_ExpiryDate + '\'' +
                ", CD_Status='" + CD_Status + '\'' +
                ", reason='" + reason + '\'' +
                ", Admin_Id=" + Admin_Id +
                '}';
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getLanguage_speak() {
        return language_speak;
    }

    public void setLanguage_speak(String language_speak) {
        this.language_speak = language_speak;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getCD_ID() {
        return CD_ID;
    }

    public void setCD_ID(int CD_ID) {
        this.CD_ID = CD_ID;
    }

    public String getCD_Name() {
        return CD_Name;
    }

    public void setCD_Name(String CD_Name) {
        this.CD_Name = CD_Name;
    }

    public String getCD_BOD() {
        return CD_BOD;
    }

    public void setCD_BOD(String CD_BOD) {
        this.CD_BOD = CD_BOD;
    }

    public int getCD_LicenseNo() {
        return CD_LicenseNo;
    }

    public void setCD_LicenseNo(int CD_LicenseNo) {
        this.CD_LicenseNo = CD_LicenseNo;
    }

    public String getCD_License_ExpiryDate() {
        return CD_License_ExpiryDate;
    }

    public void setCD_License_ExpiryDate(String CD_License_ExpiryDate) {
        this.CD_License_ExpiryDate = CD_License_ExpiryDate;
    }

    public String getCD_Status() {
        return CD_Status;
    }

    public void setCD_Status(String CD_Status) {
        this.CD_Status = CD_Status;
    }

    public int getAdmin_Id() {
        return Admin_Id;
    }

    public void setAdmin_Id(int admin_Id) {
        Admin_Id = admin_Id;
    }

}
