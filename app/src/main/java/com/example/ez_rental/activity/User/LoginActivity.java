
package com.example.ez_rental.activity.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;
import com.example.ez_rental.app.AppController;
import com.example.ez_rental.activity.helper.SQLiteHelper;
import com.example.ez_rental.activity.helper.SessionManager;

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
    private ImageView back;
    private int count =0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Register Button
        register = findViewById(R.id.register);
        String userrole;
        //Login Button
        login = findViewById(R.id.login);
        email = findViewById( R.id.email);
        password  = findViewById(R.id.password);
        forgotPass = findViewById(R.id.forgot_password);
        back = findViewById(R.id.btnbackk);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // SQLite database handler
        db = new SQLiteHelper(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        HashMap<String, String> admin = db.getAdminDetails();

        if(user.get("User_ID")!= null) {
            userrole = "user";
        }else if(admin.get("Admin_Id") != null){
            userrole = "admin";
        }else
        {
            userrole = "guest";
        }

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn() && userrole.contains("user")) {

            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this,UserViewActivity.class);
            startActivity(intent);
            finish();
        }else{
            session.setLogin(false);

        }
        back.setOnClickListener(v -> finish());
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
        password.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= ( password.getRight() -  password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (count == 0){
                        password.setInputType(InputType.TYPE_CLASS_TEXT);
                        password.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_offeye), null);
                        count = 1;
                    }else{
                        count = 0;
                        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        password.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_eye), null);
                    }
                    return true;
                }
            }
            return false;
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
