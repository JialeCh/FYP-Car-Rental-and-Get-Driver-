package com.example.ez_rental.Model;

public class Brand {
    private String Brand_Id;
    private String Brand_Name;
    private String Creation_Date;
    private String Updated_Date;
    private String Admin_Id;

    public Brand(String brand_Id, String brand_Name, String creation_Date, String updated_Date, String admin_Id) {
        Brand_Id = brand_Id;
        Brand_Name = brand_Name;
        Creation_Date = creation_Date;
        Updated_Date = updated_Date;
        Admin_Id = admin_Id;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "Brand_Id='" + Brand_Id + '\'' +
                ", Brand_Name='" + Brand_Name + '\'' +
                ", Creation_Date='" + Creation_Date + '\'' +
                ", Updated_Date='" + Updated_Date + '\'' +
                ", Admin_Id='" + Admin_Id + '\'' +
                '}';
    }

    public String getBrand_Id() {
        return Brand_Id;
    }

    public void setBrand_Id(String brand_Id) {
        Brand_Id = brand_Id;
    }

    public String getBrand_Name() {
        return Brand_Name;
    }

    public void setBrand_Name(String brand_Name) {
        Brand_Name = brand_Name;
    }

    public String getCreation_Date() {
        return Creation_Date;
    }

    public void setCreation_Date(String creation_Date) {
        Creation_Date = creation_Date;
    }

    public String getUpdated_Date() {
        return Updated_Date;
    }

    public void setUpdated_Date(String updated_Date) {
        Updated_Date = updated_Date;
    }

    public String getAdmin_Id() {
        return Admin_Id;
    }

    public void setAdmin_Id(String admin_Id) {
        Admin_Id = admin_Id;
    }
}
