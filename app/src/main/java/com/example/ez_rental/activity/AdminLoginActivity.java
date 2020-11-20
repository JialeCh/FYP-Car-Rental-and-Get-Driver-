
package com.example.ez_rental.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.User.GuestViewActivity;
import com.example.ez_rental.activity.User.RegisterActivity;
import com.example.ez_rental.activity.helper.SQLiteHelper;
import com.example.ez_rental.activity.helper.SessionManager;
import com.example.ez_rental.app.AppConfig;
import com.example.ez_rental.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AdminLoginActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private EditText adminId;
    private EditText password;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SessionManager session2;
    private SQLiteHelper db;
    private TextView register;

    private Button login;
    private ImageView back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        //Register Button
        register = findViewById(R.id.register);

        //Login Button
        login = findViewById(R.id.login);
        adminId= findViewById( R.id.adminID);
        password  = findViewById(R.id.password);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        back = findViewById(R.id.btnbackk2);
        back.setOnClickListener(v -> finish());

        // SQLite database handler
        db = new SQLiteHelper(getApplicationContext());

        // Session manager




        session = new SessionManager(getApplicationContext());
        session.setLogin(false);

        // Login button Click Event
        login.setOnClickListener(view -> {
            String Admin_Id = adminId.getText().toString();
            String Admin_Password = password.getText().toString();

            // Check for empty data in the form
            if (!Admin_Id.isEmpty() && !Admin_Password.isEmpty()) {
                // login user
                checkLogin(Admin_Id, Admin_Password);
            } else {
                // Prompt user to enter credentials
                Toast.makeText(getApplicationContext(),
                        "Please enter the credentials!", Toast.LENGTH_LONG)
                        .show();
            }
        });



    }


    private void checkLogin(final String Admin_Id, final String Admin_Password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_adLogin, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
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

                        JSONObject admin = jObj.getJSONObject("admin");
                        String Admin_Id = admin.getString("Admin_Id");
                        String Admin_Name = admin.getString("Admin_Name");
                        String Username = admin.getString("Username");
                        String Admin_Email = admin.getString("Admin_Email");
                        String Admin_ContactNo = admin.getString("Admin_ContactNo");
                        String Admin_BOD = admin.getString("Admin_BOD");
                        String Admin_Password = admin.getString("Admin_Password");
                        String Address = admin.getString("Address");
                        String Admin_Status = admin.getString("Admin_Status");
                        String Updated_Date = admin.getString("Updated_Date");
                        session.setLogin(true);
                        db.deleteAdmin();
                        db.addAdmin(Admin_Id,Admin_Name, Username, Admin_Email, Admin_ContactNo, Admin_BOD, Admin_Password, Address,Admin_Status,Updated_Date);
                        Intent intent = new Intent(AdminLoginActivity.this, GuestViewActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();

                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

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
                params.put("Admin_Id", Admin_Id);
                params.put("Admin_Password", Admin_Password);

                return params;
            }

        };
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
