package com.example.ez_rental.FragmentPages;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ez_rental.Model.Reservation;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.Feedback.ViewFeedbackActivity;
import com.example.ez_rental.activity.StartUp.ActivityLauncher;
import com.example.ez_rental.activity.User.SendMail;
import com.example.ez_rental.activity.User.UserViewActivity;
import com.example.ez_rental.activity.car.AddCarActivity;
import com.example.ez_rental.activity.helper.HttpParse;
import com.example.ez_rental.activity.helper.SQLiteHelper;
import com.example.ez_rental.activity.helper.SessionManager;
import com.example.ez_rental.app.AppConfig;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class HomeFragment extends Fragment {
    GridLayout mainGrid;
    TextView user_name,user_id,txtReserve,txtPayment;
    ConstraintLayout layout;
    ImageView user_photo;
    private SQLiteHelper db;
    SessionManager session;
    private ArrayList<Reservation> list = new ArrayList<>();
    private ArrayList<Reservation> newList = new ArrayList<>();
    private String image,name,id,email;
    String HttpURL = "http://192.168.1.3/android_login_api/UpdateUser.php";
    boolean check = true;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    String userrole ="";
    private int count =0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        mainGrid = view.findViewById(R.id.gridlayout);
        db = new SQLiteHelper(getContext());
        HashMap<String, String> user = db.getUserDetails();
        HashMap<String, String> admin = db.getAdminDetails();
        session = new SessionManager(getContext());
        if(user.get("User_ID")!= null) {
            userrole = "user";
        }else if(admin.get("Admin_Id") != null){
            userrole = "admin";
        }else
        {
            userrole = "guest";
        }
        if(userrole.compareTo("user") == 0){
            init( view);
        }
        else if(userrole.compareTo("admin") == 0){
            user_name = view.findViewById(R.id.user_name);
            user_id = view.findViewById(R.id.user_id);
            user_photo = view.findViewById(R.id.user_photo);
            user_name.setText(admin.get("Username"));
            user_id.setText("Admin ID"+admin.get("Admin_Id"));
            user_photo.setImageDrawable(getResources().getDrawable(R.drawable.avatar_man_1));
        }
        else if(userrole.compareTo("guest") == 0){
            user_name = view.findViewById(R.id.user_name);
            user_id = view.findViewById(R.id.user_id);
            user_photo = view.findViewById(R.id.user_photo);
            user_name.setText("Guest");
            user_id.setText("none");
           user_photo.setImageDrawable(getResources().getDrawable(R.drawable.avatar_man_1));
        }
        setToggleEvent(mainGrid);
        return view;
    }


    private void setToggleEvent(GridLayout mainGrid) {

        for (int i = 0; i < mainGrid.getChildCount(); i++) {

            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(view -> {
                if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {

                    switch (finalI){
                        case 0:
                            if(userrole.compareTo("user") == 0){
                                final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom))
                                        .setTitle("Warning Message")
                                        .setMessage("Only Admin can access to this panel !!")
                                        .setPositiveButton("Ok", null)
                                        .setIcon(getResources().getDrawable(R.drawable.ic_warning2))
                                        .show();
                                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                                positiveButton.setOnClickListener(x -> {
                                    Intent AddVehiclePage = new Intent(getActivity(), AddCarActivity.class);
                                    startActivity(AddVehiclePage);
                                    dialog.dismiss();

                                });
                            }else if(userrole.compareTo("admin") == 0){
                                Intent AddVehiclePage = new Intent(getActivity(), AddCarActivity.class);
                                startActivity(AddVehiclePage);
                              }
                            else {
                                final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom))
                                    .setTitle("Warning Message")
                                    .setMessage("Only Admin can access to this panel !!")
                                    .setPositiveButton("Ok", null)
                                    .setIcon(getResources().getDrawable(R.drawable.ic_warning2))
                                    .show();
                                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                                positiveButton.setOnClickListener(x -> {
                                    dialog.dismiss();

                                });

                            } break;

                        case 1:
                            if(userrole.contains("user")){
                            Intent FeedbackPage = new Intent(getActivity(), ViewFeedbackActivity.class);
                            startActivity(FeedbackPage);}
                            else{
                                final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom))
                                        .setTitle("Warning Message")
                                        .setMessage("Only user can perform this function !!")
                                        .setPositiveButton("Ok", null)
                                        .setIcon(getResources().getDrawable(R.drawable.ic_warning2))
                                        .show();
                                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                                positiveButton.setOnClickListener(x -> {
                                    dialog.dismiss();

                                });
                            }
                            break;
                        case 2:{
                            if(userrole.contains("user")){
                                final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom))
                                        .setTitle("Alert Message")
                                        .setMessage("Deactivate Your Account ?")
                                        .setPositiveButton("Yes", null)
                                        .setNegativeButton("No", null)
                                        .setIcon(getResources().getDrawable(R.drawable.ic_warning2))
                                        .show();
                                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                                positiveButton.setOnClickListener(x -> {
                                    dialog.dismiss();
                                    cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
                                    PasswordUpdate(id, "Deactivate");
                                    sendEmail(email,"Deactivate");
                                    session.setLogin(false);
                                    Intent loginPage = new Intent(getActivity(), ActivityLauncher.class);
                                    loginPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(loginPage);
                                });
                                negativeButton.setOnClickListener(x -> {
                                    cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
                                    dialog.dismiss();
                                });
                            }else{
                                final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom))
                                        .setTitle("Warning Message")
                                        .setMessage("Only user can perform this function !!")
                                        .setPositiveButton("Ok", null)
                                        .setIcon(getResources().getDrawable(R.drawable.ic_warning2))
                                        .show();
                                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                                positiveButton.setOnClickListener(x -> {
                                    dialog.dismiss();

                                });
                            }

                            break;
                        }
                        case 3:{
                            final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom))
                                    .setTitle("Alert Message")
                                    .setMessage("Confirm to Log Out ?")
                                    .setPositiveButton("Yes", null)
                                    .setNegativeButton("No", null)
                                    .setIcon(getResources().getDrawable(R.drawable.ic_warning))
                                    .show();
                            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                            positiveButton.setOnClickListener(x -> {
                                dialog.dismiss();
                                if(userrole.contains("user")){
                                    db.deleteUsers();
                                }
                                else if(userrole.contains("admin")){
                                    db.deleteAdmin();
                                }

                                session.setLogin(false);
                                cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
                                Intent loginPage = new Intent(getActivity(), ActivityLauncher.class);
                                loginPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(loginPage);
                            });
                            negativeButton.setOnClickListener(x -> {
                                cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));
                                dialog.dismiss();
                            });
                            break;
                        }

                    }

                } else {
                    //Change background color
                    cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    Toast.makeText(getContext(),"This is activity from card item index  "+ finalI,Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    private void init(View view){
       user_name = view.findViewById(R.id.user_name);
        user_id = view.findViewById(R.id.user_id);
        user_photo = view.findViewById(R.id.user_photo);
        txtReserve = view.findViewById(R.id.txtReserva);
        txtPayment = view.findViewById(R.id.txtPayment);
        db = new SQLiteHelper(getContext());
        HashMap<String, String> user = db.getUserDetails();
        layout = view.findViewById(R.id.layout);
        layout.setOnClickListener(v -> {
            if (check) {
                UserViewActivity.hideBottomNav();
                check = false;
            }else{
                UserViewActivity.showBottomNav();
                check=true;
            }
        });
        id = user.get("User_ID");
        name =user.get("Username");
        image=user.get("User_Profile");
        email = user.get("User_Email");
        user_name.setText(name);
        user_id.setText("User ID: "+id);
        Picasso.get().load( image).centerCrop().resize(100, 60).into(user_photo);
        loadProducts();

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
                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }
        PasswordUpdateClass UpdateClass = new  PasswordUpdateClass();
        UpdateClass.execute(User_ID,newPassword);
    }

    private void sendEmail(String User_Email, String code) {

        SendMail sm = new SendMail(getContext(), User_Email, code);

        sm.execute();
    }

    private void loadProducts() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_Reservation,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        list.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject product = array.getJSONObject(i);
                            list.add(new Reservation(
                                    product.getInt("Reserve_ID"),
                                    product.getString("Reserve_Date"),
                                    product.getString("Rent_Date"),
                                    product.getString("Return_Date"),
                                    product.getString("Rent_Place"),
                                    product.getString("Return_Place"),
                                    product.getDouble("Total_Amount"),
                                    product.getInt("User_ID"),
                                    product.getInt("Car_Id"),
                                    product.getString("Car_Name"),
                                    product.getInt("Admin_Id"),
                                    product.getString("Status"),
                                    product.getString("Insurance"),
                                    product.getString("Driver_Name"),
                                    product.getString("Driver_ContactNo"),
                                    product.getString("Driver_Email")
                            ));

                        }
                        newList.clear();
                        Log.e(TAG,list.size()+"");
                        Log.e(TAG,email);
                        for(int i=0; i<list.size();i++){
                            String user_id = String.valueOf(list.get(i).getUser_ID());
                            String status  = list.get(i).getStatus();
                            if(user_id.contains(id) && status.contains("Completed")){
                                newList.add(list.get(i)) ;
                            }
                            else if(user_id.contains(id) && status.contains("Cash") ||user_id.contains(id) && status.contains("Payment") ){
                                count = count +1;
                            }
                        }
                        txtReserve.setText(newList.size()+"");
                        txtPayment.setText(count+"");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                });

        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
}