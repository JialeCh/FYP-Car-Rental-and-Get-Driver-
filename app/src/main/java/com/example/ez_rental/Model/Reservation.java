package com.example.ez_rental.Model;

import java.io.Serializable;

public class Reservation implements Serializable {

    private int Reserve_ID;
    private String Reserve_Date;
    private String Rent_Date;
    private String Return_Date;
    private String Rent_Place;
    private String Return_Place;
    private double Total_Amount;
    private int User_ID;
    private int Car_Id;
    private String Car_Name;
    private int Admin_Id;
    private String Status;
    private String Insurance;
    private String Driver_Name;
    private String Driver_ContactNo;
    private String Driver_Email;

    public Reservation(int reserve_ID, String reserve_Date, String rent_Date, String return_Date, String rent_Place, String return_Place, double total_Amount, int user_ID, int car_Id, String car_Name, int admin_Id, String status, String insurance, String driver_Name, String driver_ContactNo, String driver_Email) {
        Reserve_ID = reserve_ID;
        Reserve_Date = reserve_Date;
        Rent_Date = rent_Date;
        Return_Date = return_Date;
        Rent_Place = rent_Place;
        Return_Place = return_Place;
        Total_Amount = total_Amount;
        User_ID = user_ID;
        Car_Id = car_Id;
        Car_Name = car_Name;
        Admin_Id = admin_Id;
        Status = status;
        Insurance = insurance;
        Driver_Name = driver_Name;
        Driver_ContactNo = driver_ContactNo;
        Driver_Email = driver_Email;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "Reserve_ID=" + Reserve_ID +
                ", Reserve_Date='" + Reserve_Date + '\'' +
                ", Rent_Date='" + Rent_Date + '\'' +
                ", Return_Date='" + Return_Date + '\'' +
                ", Rent_Place='" + Rent_Place + '\'' +
                ", Return_Place='" + Return_Place + '\'' +
                ", Total_Amount=" + Total_Amount +
                ", User_ID=" + User_ID +
                ", Car_Id=" + Car_Id +
                ", Car_Name='" + Car_Name + '\'' +
                ", Admin_Id=" + Admin_Id +
                ", Status='" + Status + '\'' +
                ", Insurance='" + Insurance + '\'' +
                ", Driver_Name='" + Driver_Name + '\'' +
                ", Driver_ContactNo='" + Driver_ContactNo + '\'' +
                ", Driver_Email='" + Driver_Email + '\'' +
                '}';
    }

    public String getDriver_Name() {
        return Driver_Name;
    }

    public void setDriver_Name(String driver_Name) {
        Driver_Name = driver_Name;
    }

    public String getDriver_ContactNo() {
        return Driver_ContactNo;
    }

    public void setDriver_ContactNo(String driver_ContactNo) {
        Driver_ContactNo = driver_ContactNo;
    }

    public String getDriver_Email() {
        return Driver_Email;
    }

    public void setDriver_Email(String driver_Email) {
        Driver_Email = driver_Email;
    }

    public int getReserve_ID() {
        return Reserve_ID;
    }

    public void setReserve_ID(int reserve_ID) {
        Reserve_ID = reserve_ID;
    }

    public String getCar_Name() {
        return Car_Name;
    }

    public void setCar_Name(String car_Name) {
        Car_Name = car_Name;
    }



    public String getReserve_Date() {
        return Reserve_Date;
    }

    public void setReserve_Date(String reserve_Date) {
        Reserve_Date = reserve_Date;
    }

    public String getRent_Date() {
        return Rent_Date;
    }

    public void setRent_Date(String rent_Date) {
        Rent_Date = rent_Date;
    }

    public String getReturn_Date() {
        return Return_Date;
    }

    public void setReturn_Date(String return_Date) {
        Return_Date = return_Date;
    }

    public String getRent_Place() {
        return Rent_Place;
    }

    public void setRent_Place(String rent_Place) {
        Rent_Place = rent_Place;
    }

    public String getReturn_Place() {
        return Return_Place;
    }

    public void setReturn_Place(String return_Place) {
        Return_Place = return_Place;
    }

    public double getTotal_Amount() {
        return Total_Amount;
    }

    public void setTotal_Amount(double total_Amount) {
        Total_Amount = total_Amount;
    }

    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    public int getCar_Id() {
        return Car_Id;
    }

    public void setCar_Id(int car_Id) {
        Car_Id = car_Id;
    }

    public int getAdmin_Id() {
        return Admin_Id;
    }

    public void setAdmin_Id(int admin_Id) {
        Admin_Id = admin_Id;
    }



    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getInsurance() {
        return Insurance;
    }

    public void setInsurance(String insurance) {
        Insurance = insurance;
    }
}
