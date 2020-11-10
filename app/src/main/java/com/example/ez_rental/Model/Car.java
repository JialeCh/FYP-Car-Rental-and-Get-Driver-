package com.example.ez_rental.Model;


import java.io.Serializable;

public class Car  implements Serializable {
    private int Car_Id;
    private String Car_Title;
    private String Car_Overview;
    private double PricePerDay;
    private String Fuel_Type;
    private int ModelYear;
    private int Seating_Cap;
    private String VImage1;
    private String VImage2;
    private String VImage3;
    private String RegDate;
    private String UpdationDate;
    private String Car_Status;
    private int Admin_Id;
    private int Brand_Id;
    private String Brand_Name;
    private String Location;
    private String plate_no;
    private String color;
    private int rating;
    private String reason;


    public Car(int car_Id, String car_Title, String car_Overview, double pricePerDay, String fuel_Type, int modelYear, int seating_Cap, String VImage1, String VImage2, String VImage3, String regDate, String updationDate, String car_Status, int admin_Id, int brand_Id, String brand_Name, String location, String plate_no, String color, int rating, String reason) {
        Car_Id = car_Id;
        Car_Title = car_Title;
        Car_Overview = car_Overview;
        PricePerDay = pricePerDay;
        Fuel_Type = fuel_Type;
        ModelYear = modelYear;
        Seating_Cap = seating_Cap;
        this.VImage1 = VImage1;
        this.VImage2 = VImage2;
        this.VImage3 = VImage3;
        RegDate = regDate;
        UpdationDate = updationDate;
        Car_Status = car_Status;
        Admin_Id = admin_Id;
        Brand_Id = brand_Id;
        Brand_Name = brand_Name;
        Location = location;
        this.plate_no = plate_no;
        this.color = color;
        this.rating = rating;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Car{" +
                "Car_Id=" + Car_Id +
                ", Car_Title='" + Car_Title + '\'' +
                ", Car_Overview='" + Car_Overview + '\'' +
                ", PricePerDay=" + PricePerDay +
                ", Fuel_Type='" + Fuel_Type + '\'' +
                ", ModelYear=" + ModelYear +
                ", Seating_Cap=" + Seating_Cap +
                ", VImage1='" + VImage1 + '\'' +
                ", VImage2='" + VImage2 + '\'' +
                ", VImage3='" + VImage3 + '\'' +
                ", RegDate='" + RegDate + '\'' +
                ", UpdationDate='" + UpdationDate + '\'' +
                ", Car_Status='" + Car_Status + '\'' +
                ", Admin_Id=" + Admin_Id +
                ", Brand_Id=" + Brand_Id +
                ", Brand_Name='" + Brand_Name + '\'' +
                ", Location='" + Location + '\'' +
                ", plate_no='" + plate_no + '\'' +
                ", color='" + color + '\'' +
                ", rating=" + rating +
                ", reason='" + reason + '\'' +
                '}';
    }

    public int getAdmin_Id() {
        return Admin_Id;
    }

    public void setAdmin_Id(int admin_Id) {
        Admin_Id = admin_Id;
    }

    public int getBrand_Id() {
        return Brand_Id;
    }

    public void setBrand_Id(int brand_Id) {
        Brand_Id = brand_Id;
    }

    public String getBrand_Name() {
        return Brand_Name;
    }

    public void setBrand_Name(String brand_Name) {
        Brand_Name = brand_Name;
    }

    public String getPlate_no() {
        return plate_no;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }


    public String getObject(){
        String str = "Car c1 = new Car("+Car_Id+","+ Car_Title+","+ Car_Overview+","+PricePerDay+","+Fuel_Type+"" +
                ","+ModelYear+","+Seating_Cap+","+VImage1+","+VImage2+","+VImage3+","+RegDate+","+UpdationDate+","+Location+"\");";
        return str;
    }



    public int getCar_Id() {
        return Car_Id;
    }

    public void setCar_Id(int car_Id) {
        Car_Id = car_Id;
    }

    public String getCar_Title() {
        return Car_Title;
    }

    public void setCar_Title(String car_Title) {
        Car_Title = car_Title;
    }

    public String getCar_Overview() {
        return Car_Overview;
    }

    public void setCar_Overview(String car_Overview) {
        Car_Overview = car_Overview;
    }

    public double getPricePerDay() {
        return PricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        PricePerDay = pricePerDay;
    }

    public String getFuel_Type() {
        return Fuel_Type;
    }

    public void setFuel_Type(String fuel_Type) {
        Fuel_Type = fuel_Type;
    }

    public int getModelYear() {
        return ModelYear;
    }

    public void setModelYear(int modelYear) {
        ModelYear = modelYear;
    }

    public int getSeating_Cap() {
        return Seating_Cap;
    }

    public void setSeating_Cap(int seating_Cap) {
        Seating_Cap = seating_Cap;
    }

    public String getVImage1() {
        return VImage1;
    }

    public void setVImage1(String VImage1) {
        this.VImage1 = VImage1;
    }

    public String getVImage2() {
        return VImage2;
    }

    public void setVImage2(String VImage2) {
        this.VImage2 = VImage2;
    }

    public String getVImage3() {
        return VImage3;
    }

    public void setVImage3(String VImage3) {
        this.VImage3 = VImage3;
    }

    public String getRegDate() {
        return RegDate;
    }

    public void setRegDate(String regDate) {
        RegDate = regDate;
    }

    public String getUpdationDate() {
        return UpdationDate;
    }

    public void setUpdationDate(String updationDate) {
        UpdationDate = updationDate;
    }
    public String getCar_Status() {
        return Car_Status;
    }

    public void setCar_Status(String car_Status) {
        Car_Status = car_Status;
    }


}
