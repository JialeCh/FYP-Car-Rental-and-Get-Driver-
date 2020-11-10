package com.example.ez_rental.Model;

public class Feedback {
    private int Feedback_ID;
    private int Reserve_Id;
    private int Car_Id;
    private String User_Feedback;
    private int User_Rating;
    private String Feedback_Date;
    private int User_ID;
    private int Admin_ID;

    public Feedback(int feedback_ID, int reserve_Id, int car_Id, String user_Feedback, int user_Rating, String feedback_Date, int user_ID, int admin_ID) {
        Feedback_ID = feedback_ID;
        Reserve_Id = reserve_Id;
        Car_Id = car_Id;
        User_Feedback = user_Feedback;
        User_Rating = user_Rating;
        Feedback_Date = feedback_Date;
        User_ID = user_ID;
        Admin_ID = admin_ID;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "Feedback_ID=" + Feedback_ID +
                ", Reserve_Id=" + Reserve_Id +
                ", Car_Id=" + Car_Id +
                ", User_Feedback='" + User_Feedback + '\'' +
                ", User_Rating=" + User_Rating +
                ", Feedback_Date='" + Feedback_Date + '\'' +
                ", User_ID=" + User_ID +
                ", Admin_ID=" + Admin_ID +
                '}';
    }

    public int getCar_Id() {
        return Car_Id;
    }

    public void setCar_Id(int car_Id) {
        Car_Id = car_Id;
    }

    public int getFeedback_ID() {
        return Feedback_ID;
    }

    public void setFeedback_ID(int feedback_ID) {
        Feedback_ID = feedback_ID;
    }

    public int getReserve_Id() {
        return Reserve_Id;
    }

    public void setReserve_Id(int reserve_Id) {
        Reserve_Id = reserve_Id;
    }

    public String getUser_Feedback() {
        return User_Feedback;
    }

    public void setUser_Feedback(String user_Feedback) {
        User_Feedback = user_Feedback;
    }

    public int getUser_Rating() {
        return User_Rating;
    }

    public void setUser_Rating(int user_Rating) {
        User_Rating = user_Rating;
    }

    public String getFeedback_Date() {
        return Feedback_Date;
    }

    public void setFeedback_Date(String feedback_Date) {
        Feedback_Date = feedback_Date;
    }

    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    public int getAdmin_ID() {
        return Admin_ID;
    }

    public void setAdmin_ID(int admin_ID) {
        Admin_ID = admin_ID;
    }
}
