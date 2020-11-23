package com.example.ez_rental.FragmentPages;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.ez_rental.Model.User;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.StartUp.ActivityLauncher;
import com.example.ez_rental.activity.User.UpdateActivity;
import com.example.ez_rental.activity.helper.HttpParse;
import com.example.ez_rental.activity.helper.SQLiteHelper;
import com.example.ez_rental.activity.helper.SessionManager;
import com.example.ez_rental.app.AppConfig;
import com.example.ez_rental.app.AppController;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class AccountFragment extends Fragment {

    private ImageView logout,back;
    private TextView edit,btnChangePass,expiryDate;
    private EditText Usernametxt, email, contact_no, address, license_no;
    private TextView email2;
    private ImageView profileImage;
    private SQLiteHelper db;
    private SessionManager session;
    private Button save,upload;
    private String activity;
    private String User_ID, Username, User_Email, User_ContactNo, Address, User_Password,User_Profile, Driver_license ,License_ExpiryDate;
    private String User_ID2, Username2, User_Email2, User_ContactNo2, Address2, User_Password2,User_Profile2, Driver_license2 ,License_ExpiryDate2;
    private Bitmap FixBitmap;
    private byte[] image;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);

    String userrole;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    private static final int IMAGE_PICKER_SELECT = 999;
    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_profile_main, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        session = new SessionManager(getContext());
        db = new SQLiteHelper(getContext());

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
        if(userrole.contains("user")){
            initComponents(view);
            displayUserInfo();

            try {
                listenHandler();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            expiryDate.setClickable(true);
            activity="nothing";
        }else{
            final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom))
                    .setTitle("Warning")
                    .setMessage("You are not login as user")
                    .setPositiveButton("Ok", null)
                    .setIcon(getResources().getDrawable(R.drawable.ic_warning2))
                    .show();
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener(x -> {
                dialog.dismiss();
                HomeFragment fragment1 = new  HomeFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout, fragment1);
                fragmentTransaction.commit();
            });

        }



        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listenHandler() throws ParseException {
        back.setOnClickListener(v -> displayUserInfo());
        logout.setOnClickListener(v -> {

            session.setLogin(false);

            Intent loginPage = new Intent(getActivity(), ActivityLauncher.class);
            loginPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginPage);
        });
        edit.setOnClickListener(v -> {
                editInfo();

            refresh(User_Email2, User_Password2);});
        upload.setOnClickListener(view -> {

            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, IMAGE_PICKER_SELECT);


        });
        save.setOnClickListener(v -> {
            undoInfo();
            final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom))
                    .setTitle("Alert Message")
                    .setMessage("Save details?")
                    .setPositiveButton("Ok", null)
                    .setNegativeButton("Cancel", null)
                    .setIcon(getResources().getDrawable(R.drawable.ic_save))
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar licenseDate = Calendar.getInstance();
        licenseDate.setTime(sdf.parse(License_ExpiryDate2));
        expiryDate.setOnClickListener(v ->openCalendar(licenseDate,expiryDate));
        btnChangePass.setOnClickListener(v -> {
            undoInfo();
            AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(getContext());
            final int[] count = {0};
            @SuppressLint("InflateParams") View mCustomView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
            final EditText mEditOld = mCustomView.findViewById(R.id.editOldPass);
            final EditText mEditNew= mCustomView.findViewById(R.id.editTextPassword);
            final EditText mEditNew2= mCustomView.findViewById(R.id.editTextPassword2);
            mEditNew2.setBackgroundResource(android.R.drawable.editbox_background);
            mEditNew.setBackgroundResource(android.R.drawable.editbox_background);
            mEditOld.setBackgroundResource(android.R.drawable.editbox_background);
            Button mButtonLogin = mCustomView.findViewById(R.id.buttonLogin);
            ImageView close = mCustomView.findViewById(R.id.btnClose);
            TextView msg = mCustomView.findViewById(R.id.txtMsg);
            mAlertDialogBuilder.setView(mCustomView);
            final AlertDialog dialog = mAlertDialogBuilder.create();
            dialog.show();
            close.setOnClickListener(v2->dialog.dismiss());
            mEditNew2.setOnTouchListener((v2, event) -> {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= ( mEditNew2.getRight() -  mEditNew2.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (count[0] == 0){
                            mEditNew2.setInputType(InputType.TYPE_CLASS_TEXT);
                            mEditNew2.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(),R.drawable.ic_offeye), null);
                            count[0] = 1;
                        }else{
                            count[0] = 0;
                            mEditNew2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            mEditNew2.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(),R.drawable.ic_eye), null);
                        }
                        return true;
                    }
                }
                return false;
            });
            mEditNew.setOnTouchListener((v2, event) -> {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= ( mEditNew.getRight() -  mEditNew.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (count[0] == 0){
                            mEditNew.setInputType(InputType.TYPE_CLASS_TEXT);
                            mEditNew.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(),R.drawable.ic_offeye), null);
                            count[0] = 1;
                        }else{
                            count[0] = 0;
                            mEditNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            mEditNew.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(),R.drawable.ic_eye), null);
                        }
                        return true;
                    }
                }
                return false;
            });
            mEditOld.setOnTouchListener((v2, event) -> {
                final int DRAWABLE_RIGHT = 2;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (  mEditOld.getRight() -   mEditOld.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        if (count[0] == 0){
                            mEditOld.setInputType(InputType.TYPE_CLASS_TEXT);
                            mEditOld.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(),R.drawable.ic_offeye), null);
                            count[0] = 1;
                        }else{
                            count[0] = 0;
                            mEditOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            mEditOld.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getContext(),R.drawable.ic_eye), null);
                        }
                        return true;
                    }
                }
                return false;
            });
            mButtonLogin.setOnClickListener(v1 -> {
                if (mEditOld.getText().toString().compareTo(User_Password2) == 0){
                    if (!mEditOld.getText().toString().isEmpty() && !mEditNew.getText().toString().isEmpty()&& !mEditNew2.getText().toString().isEmpty()){
                        if(mEditNew.getText().toString().compareTo(mEditNew2.getText().toString()) == 0){
                            PasswordUpdate(User_ID2, mEditNew2.getText().toString());
                            dialog.dismiss();
                            msg.setText(" ");
                            session.setLogin(false);

                            Intent loginPage = new Intent(getActivity(), ActivityLauncher.class);
                            loginPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginPage);
                        }else {
                            msg.setText("Password Mismatch !!");
                        }

                    }else {
                        msg.setText("Please enter the required information! ");
                    }
                }else {
                    msg.setText("Wrong Old Password !");
                }


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
        btnChangePass = view.findViewById(R.id.btnChangPass);
        expiryDate = view.findViewById(R.id.expiry_dat);

    }

    public void PasswordUpdate(final String User_ID, final String newPassword) {
        class PasswordUpdateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                Toast.makeText(getContext(),"Password Changed Successful !",Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {
                hashMap.put("User_ID", params[0]);
                hashMap.put("User_Password", params[1]);
                finalResult = httpParse.postRequest(hashMap,AppConfig.Url_UpdateUser);

                return finalResult;
            }
        }
        PasswordUpdateClass UpdateClass = new  PasswordUpdateClass();
        UpdateClass.execute(User_ID,newPassword);
    }
    private void displayUserInfo() {

        GetDataFromSQLite();
        Picasso.get().load(User_Profile2).into(profileImage);
        Usernametxt.setText(Username2);
        email.setText(User_Email2);
        email2.setText(User_Email2);
        contact_no.setText(User_ContactNo2);
        address.setText(Address2);
        license_no.setText(Driver_license2);
        expiryDate.setText(" "+License_ExpiryDate2);
    }

    private void editInfo()  {
        btnChangePass.setVisibility(View.INVISIBLE);
        expiryDate.setClickable(true);
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
        expiryDate.setEnabled(true);
        address.setBackgroundResource(android.R.drawable.editbox_background);
        save.setVisibility(View.VISIBLE);
        upload.setVisibility(View.VISIBLE);
    }

    private void undoInfo() {
        btnChangePass.setVisibility(View.VISIBLE);
        contact_no.setEnabled(false);
        expiryDate.setClickable(false);
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
    private void openCalendar(final Calendar rentalDate, final TextView rentalDateText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());

        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            rentalDate.set(year,month,dayOfMonth);
            rentalDateText.setText(dateFormat.format(rentalDate.getTime()));
        });
        datePickerDialog.show();
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
        License_ExpiryDate=  expiryDate.getText().toString();
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
        refresh(User_Email2, User_Password2);
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



