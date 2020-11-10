package com.example.ez_rental.Model;

import java.io.Serializable;

public class Payments implements Serializable {

    private String Payment_ID;
    private double Payment_Amount;
    private String Payment_Date;
    private String Payment_Method;
    private String Payment_Status;
    private int Card_No;
    private int Reserve_ID;
    private int User_ID;

    public Payments(String payment_ID, double payment_Amount, String payment_Date, String payment_Method, String payment_Status, int card_No, int reserve_ID, int user_ID) {
        Payment_ID = payment_ID;
        Payment_Amount = payment_Amount;
        Payment_Date = payment_Date;
        Payment_Method = payment_Method;
        Payment_Status = payment_Status;
        Card_No = card_No;
        Reserve_ID = reserve_ID;
        User_ID = user_ID;
    }

    @Override
    public String toString() {
        return "Payments{" +
                "Payment_ID='" + Payment_ID + '\'' +
                ", Payment_Amount=" + Payment_Amount +
                ", Payment_Date='" + Payment_Date + '\'' +
                ", Payment_Method='" + Payment_Method + '\'' +
                ", Payment_Status='" + Payment_Status + '\'' +
                ", Card_No=" + Card_No +
                ", Reserve_ID=" + Reserve_ID +
                ", User_ID=" + User_ID +
                '}';
    }

    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }



    public String getPayment_ID() {
        return Payment_ID;
    }

    public void setPayment_ID(String payment_ID) {
        Payment_ID = payment_ID;
    }

    public String getPayment_Date() {
        return Payment_Date;
    }

    public void setPayment_Date(String payment_Date) {
        Payment_Date = payment_Date;
    }

    public String getPayment_Method() {
        return Payment_Method;
    }

    public void setPayment_Method(String payment_Method) {
        Payment_Method = payment_Method;
    }

    public String getPayment_Status() {
        return Payment_Status;
    }

    public void setPayment_Status(String payment_Status) {
        Payment_Status = payment_Status;
    }

    public int getCard_No() {
        return Card_No;
    }

    public void setCard_No(int card_No) {
        Card_No = card_No;
    }

    public int getReserve_ID() {
        return Reserve_ID;
    }

    public void setReserve_ID(int reserve_ID) {
        Reserve_ID = reserve_ID;
    }

    public double getPayment_Amount() {
        return Payment_Amount;
    }

    public void setPayment_Amount(double payment_Amount) {
        Payment_Amount = payment_Amount;
    }
}
