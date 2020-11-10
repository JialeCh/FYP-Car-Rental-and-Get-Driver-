
package com.example.ez_rental.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.example.ez_rental.Model.User;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;
import com.example.ez_rental.app.AppController;
import com.example.ez_rental.helper.SQLiteHelper;
import com.example.ez_rental.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private TextView btnLinkToLogin;
    private TextView expiryDate;
    private TextView dob;
    private ImageView profile;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHelper db;
    private String User_ID;
    private String Username;
    private String User_Email;
    private String User_ContactNo;
    private String activity="";
    private String Address;
    private String User_Password;
    private String User_Profile ;
    private String Driver_license ;
    Bitmap FixBitmap;
    byte[] image;
    private static final int IMAGE_PICKER_SELECT = 999;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        User_ID = generateID();
        TextView tUID = (TextView) findViewById(R.id.User_ID);
        tUID.setText( User_ID);

        btnRegister = (Button) findViewById(R.id.register);
        btnLinkToLogin = (TextView) findViewById(R.id.login);
        profile = (ImageView)findViewById((R.id.profileUser));
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new SQLiteHelper(getApplicationContext());
        profile.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, IMAGE_PICKER_SELECT);

        });
        btnRegister.setOnClickListener(view -> {
            Username = ((EditText)findViewById(R.id.firstName)).getText().toString().trim()+ ((EditText)findViewById(R.id.lastName)).getText().toString().trim();
            User_Email = ((EditText)findViewById(R.id.adminID)).getText().toString().trim();
            String User_Password = ((EditText)findViewById(R.id.password)).getText().toString().trim();
            String User_Password2 = ((EditText)findViewById(R.id.confirmPassword)).getText().toString().trim();
            User_ContactNo = ((EditText)findViewById(R.id.ContactNo)).getText().toString().trim();
            String addr = ((EditText)findViewById(R.id.addressText)).getText().toString().trim();
            String street = ((EditText)findViewById(R.id.street)).getText().toString().trim();
            Driver_license  = ((EditText)findViewById(R.id.user_license_no)).getText().toString().trim();
            String exdate = ((EditText)findViewById(R.id.user_license_expiry)).getText().toString().trim();
            Address = addr + street ;User_Profile=" ";
            if (!Username.isEmpty() && !User_Email.isEmpty() && !User_Password.isEmpty()) {
                if(User_Password.compareTo(User_Password2) == 0){
                    registerUser(User_ID,Username,User_Email,User_ContactNo,Address,User_Password,User_Profile,Driver_license,exdate);
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Confirm Password different with Password !", Toast.LENGTH_LONG)
                            .show();
                }

            } else {
                Toast.makeText(getApplicationContext(),
                        "Please enter your details!", Toast.LENGTH_LONG)
                        .show();
            }
        });

        btnLinkToLogin.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(),
                    LoginActivity.class);
            startActivity(i);
            finish();
        });

    }
    private String generateID(){
        Random rnd = new Random();
        int id = 202000 + rnd.nextInt(65)*3;
        String userid = String.valueOf(id);
        return userid;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == IMAGE_PICKER_SELECT && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {

                ContentResolver resolver = getApplication().getContentResolver();
                FixBitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
                profile.setImageBitmap(FixBitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                FixBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                image = stream.toByteArray();
                activity="User_Profile";

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void registerUser(final String User_ID, final String Username, final String User_Email, final String User_ContactNo, final String Adderess,
                              final String User_Password, final String User_Profile, final String Driver_license, final String License_ExpiryDate) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, response -> {
                    Log.d(TAG, "Register Response: " + response);
                    hideDialog();

                    int jsonStart = response.indexOf("{");
                    int jsonEnd = response.lastIndexOf("}");

                    if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                        response = response.substring(jsonStart, jsonEnd + 1);
                    } else {
                        // deal with the absence of JSON content here
                    }

                    response = response.replaceAll("<.*?>", "");
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {
                            JSONObject user = jObj.getJSONObject("user");
                            String User_ID1 = user.getString("User_ID");
                            String Username1 = user.getString("Username");
                            String User_Email1 = user.getString("User_Email");
                            String User_ContactNo1 = user.getString("User_ContactNo");
                            String Address = user.getString("Address");
                            String User_Password1 = user.getString("User_Password");
                            String User_Profile1 = user.getString("User_Profile");
                            String Driver_license1 = user.getString("Driver_license");
                            String License_ExpiryDate1 = user.getString("License_ExpiryDate");

                            Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
                            User newUser = new User(User_ID, Username, User_Email, User_ContactNo,  Adderess, User_Password,User_Profile, Driver_license ,License_ExpiryDate);
                            // Launch login activity
                            Intent Update = new Intent(getApplicationContext(), ImageUploadActivity.class);
                            Update.putExtra("Image", image);
                            Update.putExtra("Update", newUser);
                            Update.putExtra("Activity",activity);
                            startActivity(Update);
                            finish();
                        } else {

                            // Error occurred in registration. Get the error
                            // message
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }, error -> {
            Log.e(TAG, "Registration Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("User_ID", User_ID);
                params.put("Username", Username);
                params.put("User_Email", User_Email);
                params.put("User_Password", User_Password);
                params.put("User_ContactNo", User_ContactNo);
                params.put("Address", Adderess);
                params.put("User_Profile", User_Profile);
                params.put("Driver_license", Driver_license);
                params.put("License_ExpiryDate", License_ExpiryDate);
                return params;
            }

        };

/*        if(activity.contains(User_Profile)){
            Intent Update = new Intent(getApplicationContext(), ImageUploadActivity.class);
            Update.putExtra("Image", image);
            Update.putExtra("Update", newUser);
            Update.putExtra("Activity",activity);
            startActivity(Update);
        }*/

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
