package com.example.ez_rental.activity.User;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ez_rental.Model.User;
import com.example.ez_rental.activity.helper.HttpParse;
import com.example.ez_rental.app.AppConfig;

import java.util.HashMap;


public class UpdateActivity extends AppCompatActivity {


    String user_id;

    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    byte[] byteArray;

    String activity;
    User newUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        newUser = (com.example.ez_rental.Model.User) getIntent().getSerializableExtra("Update");

        user_id = newUser.getUser_ID();
        UserUpdate(newUser.getUser_ID(), newUser.getUsername(), newUser.getUser_Email(),
                newUser.getUser_ContactNo(),
                newUser.getAddress(),
                newUser.getUser_Password(), newUser.getDriver_license(),newUser.getLicense_ExpiryDate());
        activity=getIntent().getStringExtra("Activity");
        byteArray = getIntent().getByteArrayExtra("Image");


    }



    public void UserUpdate(final String User_ID, final String Username, final String User_Email, final String User_ContactNo, final String Address,
                           final String User_Password, final String Driver_license, final String License_ExpiryDate) {

        class UserUpdateClass extends AsyncTask<String, Void, String> {



            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                Toast.makeText(getApplicationContext(),"User Details Updated",Toast.LENGTH_LONG).show();
                if(activity.contains("User_Profile")){
                    Intent Update = new Intent(getApplicationContext(), ImageUploadActivity.class);
                    Update.putExtra("Image", byteArray);
                    Update.putExtra("Update", newUser);
                    Update.putExtra("Activity",activity);
                    startActivity(Update);
                    finish();
                }else{

                        finish();


                }
            }
            @Override
            protected String doInBackground(String... params) {
                hashMap.put("User_ID", params[0]);
                hashMap.put("Username", params[1]);
                hashMap.put("User_Email", params[2]);
                hashMap.put("User_ContactNo", params[3]);
                hashMap.put("Address", params[4]);
                hashMap.put("User_Password", params[5]);
                hashMap.put("Driver_license", params[6]);
                hashMap.put("License_ExpiryDate", params[7]);
                finalResult = httpParse.postRequest(hashMap, AppConfig.HttpURL_UpUser);
                return finalResult;
            }
        }
        UserUpdateClass UserUpdateClass = new UserUpdateClass();
        UserUpdateClass.execute(String.valueOf(User_ID), Username, User_Email, User_ContactNo, Address, User_Password, Driver_license,License_ExpiryDate);
    }








}