package com.example.ez_rental.FragmentPages;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.ez_rental.ActivityPages.ActivityLauncher;
import com.example.ez_rental.Model.User;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.UpdateActivity;
import com.example.ez_rental.app.AppConfig;
import com.example.ez_rental.app.AppController;
import com.example.ez_rental.helper.SQLiteHelper;
import com.example.ez_rental.helper.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class AccountFragment extends Fragment {

    private ImageView logout;
    private ImageView back;
    private TextView edit;
    private EditText Usernametxt, email, contact_no, address, license_no;
    private TextView email2;
    private ImageView profileImage;
    private SQLiteHelper db;
    private SessionManager session;
    private Button save;
    private Button upload;
    private String activity;
    String User_ID;
    String Username ;
    String User_Email;
    String User_Password;
    String User_ContactNo ;
    String Address;
    String User_Profile="";
    String Driver_license;
    String License_ExpiryDate;
    String User_ID2;
    String Username2 ;
    String User_Email2;
    String User_Password2;
    String User_ContactNo2;
    String Address2;
    String User_Profile2="";
    String Driver_license2;
    String License_ExpiryDate2;
    Bitmap FixBitmap;
    byte[] image;

    private static final int IMAGE_PICKER_SELECT = 999;
    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_main, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        initComponents(view);
        displayUserInfo();
        listenHandler();
        String email=db.getUserDetails().get("User_Email");
        String pass =db.getUserDetails().get("User_Password");

        activity="nothing";
        session = new SessionManager(getContext());
        return view;
    }

    private void listenHandler() {
        back.setOnClickListener(v -> displayUserInfo());
        logout.setOnClickListener(v -> {

            session.setLogin(false);

            Intent loginPage = new Intent(getActivity(), ActivityLauncher.class);
            loginPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginPage);
        });
        edit.setOnClickListener(v -> {editInfo();
                refresh(User_Email2, User_Password2);});
        upload.setOnClickListener(view -> {

            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, IMAGE_PICKER_SELECT);


        });
        save.setOnClickListener(v -> {
            undoInfo();
            final AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setMessage("Save details?")
                    .setPositiveButton("Ok", null)
                    .setNegativeButton("Cancel", null)
                    .show();
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            positiveButton.setOnClickListener(x -> {
                dialog.dismiss();
                GetDataFromEditText();
                Intent Update = new Intent(getActivity(), UpdateActivity.class);
                User newUser = new User(User_ID, Username, User_Email, User_ContactNo, Address, User_Password,User_Profile, Driver_license ,License_ExpiryDate);
                Update.putExtra("Update", newUser);
                Update.putExtra("Image",image);
                Update.putExtra("Activity",activity);
                startActivity(Update);
            });
            negativeButton.setOnClickListener(x -> {

                dialog.dismiss();
            });
        });
    }

    private void initComponents(View view) {
        profileImage = view.findViewById(R.id.userProfile);
        logout = view.findViewById(R.id.btnLogout);
        back = view.findViewById(R.id.bckBtn);
        Usernametxt = view.findViewById(R.id.Username);
        email2 = view.findViewById(R.id.txtEmailUser);
        email = view.findViewById(R.id.mailUser);
        contact_no = view.findViewById(R.id.Contact_No);
        edit = view.findViewById(R.id.editContact);
        address = view.findViewById(R.id.addressUser);
        license_no = view.findViewById(R.id.license_no);
        save = view.findViewById(R.id.btnSave);
        upload =view.findViewById(R.id.btnUpload);
        if (session != null)
            session.setLogin(true);
    }


    private void displayUserInfo() {
        GetDataFromSQLite();
        Picasso.get().load(User_Profile2).into(profileImage);
        Usernametxt.setText(Username2 );
        email.setText(User_Email2);
        email2.setText(User_Email2);
        contact_no.setText(User_ContactNo2);
        address.setText(Address2);
        license_no.setText(Driver_license2);
    }

    private void editInfo() {
        edit.setVisibility(View.INVISIBLE);
        contact_no.setEnabled(true);
        contact_no.setBackgroundResource(android.R.drawable.editbox_background);
        license_no.setEnabled(true);
        license_no.setBackgroundResource(android.R.drawable.editbox_background);
        Usernametxt.setEnabled(true);
        Usernametxt.setBackgroundResource(android.R.drawable.editbox_background);
        email.setEnabled(true);
        email.setBackgroundResource(android.R.drawable.editbox_background);
        address.setEnabled(true);
        address.setBackgroundResource(android.R.drawable.editbox_background);
        save.setVisibility(View.VISIBLE);
        upload.setVisibility(View.VISIBLE);
    }

    private void undoInfo() {
        contact_no.setEnabled(false);
        contact_no.setBackgroundResource(android.R.color.transparent);
        license_no.setEnabled(false);
        license_no.setBackgroundResource(android.R.color.transparent);
        Usernametxt.setEnabled(false);
        Usernametxt.setBackgroundResource(android.R.color.transparent);
        email.setEnabled(false);
        email.setBackgroundResource(android.R.color.transparent);
        address.setEnabled(false);
        address.setBackgroundResource(android.R.color.transparent);
        save.setVisibility(View.INVISIBLE);
        upload.setVisibility(View.INVISIBLE);
        edit.setVisibility(View.VISIBLE);
    }
    public void GetDataFromEditText(){
        db = new SQLiteHelper(getContext());
        HashMap<String, String> user = db.getUserDetails();
        User_ID = User_ID2;

        Username = Usernametxt.getText().toString();
        if(Username.equals("")){
            Username = Username2;
        }
        User_Email=  email.getText().toString();
        if(User_Email.equals("")){
            User_Email = User_Email2;
        }
        User_Password= user.get("User_Password");
        User_ContactNo = contact_no.getText().toString();
        if(User_ContactNo.equals("")){
            User_ContactNo = User_ContactNo2;
        }
        Address =address.getText().toString();
        if( Address.equals("")){
            Address =  Address2;
        }
        User_Profile=User_Profile2;
        Driver_license=license_no.getText().toString();
        if(Driver_license.equals("")){
            Driver_license = Driver_license2;
        }

        refresh(User_Email2, User_Password2);
    }

    public void GetDataFromSQLite(){
        db = new SQLiteHelper(getContext());
        HashMap<String, String> user = db.getUserDetails();
        User_ID2 = user.get("User_ID");
        Username2 =user.get("Username");
        User_Email2= user.get("User_Email");
        User_ContactNo2 = user.get("User_ContactNo");
        Address2 =user.get("Address");
        User_Password2= user.get("User_Password");
        User_Profile2=user.get("User_Profile");
        image= User_Profile2.getBytes();
        Driver_license2=user.get("Driver_license");
        License_ExpiryDate2 = user.get("License_ExpiryDate");
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == IMAGE_PICKER_SELECT && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {

                ContentResolver resolver = getActivity().getContentResolver();
                FixBitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
                profileImage.setImageBitmap(FixBitmap);
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
    private void refresh(final String email, final String password) {

        String tag_string_req = "req_getDetails";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, response -> {
                    Log.d(TAG, "Login Response: " + response);
                    int jsonStart = response.indexOf("{");
                    int jsonEnd = response.lastIndexOf("}");
                    if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                        response = response.substring(jsonStart, jsonEnd + 1);
                    } else {
                    }
                    response = response.replaceAll("<.*?>", "");
                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {
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
                            db.deleteUsers();
                            db.addUser(User_ID, Username, User_Email, User_ContactNo, Address, User_Password, User_Profile, Driver_license,License_ExpiryDate);
                        } else {
                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }, error -> {
            Log.e(TAG, "Login Error: " + error.getMessage());
            Toast.makeText(getContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}



