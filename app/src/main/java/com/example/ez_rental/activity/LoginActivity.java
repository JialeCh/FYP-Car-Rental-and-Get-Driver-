
package com.example.ez_rental.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.example.ez_rental.ActivityPages.UserViewActivity;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;
import com.example.ez_rental.app.AppController;
import com.example.ez_rental.helper.SQLiteHelper;
import com.example.ez_rental.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText email;
    private EditText password;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHelper db;
    private TextView register;
    private TextView forgotPass;
    private Button login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Register Button
        register = findViewById(R.id.register);

        //Login Button
        login = findViewById(R.id.login);
        email = findViewById( R.id.email);
        password  = findViewById(R.id.password);
        forgotPass = findViewById(R.id.forgot_password);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SQLite database handler
        db = new SQLiteHelper(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {

            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this,UserViewActivity.class);
            startActivity(intent);
            finish();
        }else{
            session.setLogin(false);

        }

        // Login button Click Event
        login.setOnClickListener(view -> {
            String User_Email = email.getText().toString();
            String User_Password = password.getText().toString();

            // Check for empty data in the form
            if (!User_Email.isEmpty() && !User_Password.isEmpty()) {
                // login user
                checkLogin(User_Email, User_Password);
            } else {
                // Prompt user to enter credentials
                Toast.makeText(getApplicationContext(),
                        "Please enter the credentials!", Toast.LENGTH_LONG)
                        .show();
            }
        });

        forgotPass.setOnClickListener(view ->{
            Intent i = new Intent(getApplicationContext(),
                    ForgotPassword.class);
            startActivity(i);
            finish();
        });

        // Link to Register Screen
        register.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(),
                    RegisterActivity.class);
            startActivity(i);
            finish();
        });

    }


    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, response -> {
                    Log.d(TAG, "Login Response: " + response);
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

                        // Check for error node in json
                        if (!error) {
                            // user successfully logged in
                            // Create login session
                            session.setLogin(true);

                            // Now store the user in SQLite
                            JSONObject user = jObj.getJSONObject("user");
                            String User_ID = user.getString("User_ID");
                            String Username = user.getString("Username");
                            String User_Email = user.getString("User_Email");
                            String User_ContactNo = user.getString("User_ContactNo");
                            String Address = user.getString("Address");
                            String User_Password = user.getString("User_Password");
                            String User_Profile = user.getString("User_Profile");
                            String Driver_license = user.getString("Driver_license");
                            String License_ExpiryDate = user.getString("License_ExpiryDate");
                            // Inserting row in users table
                            db.deleteUsers();
                            db.addUser(User_ID, Username, User_Email, User_ContactNo, Address, User_Password, User_Profile, Driver_license,License_ExpiryDate);
                            Intent intent = new Intent(LoginActivity.this, UserViewActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // JSON error
                        e.printStackTrace();

                        Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }, error -> {
            Log.e(TAG, "Login Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            hideDialog();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
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
