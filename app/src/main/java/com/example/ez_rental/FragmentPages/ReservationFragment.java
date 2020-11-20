package com.example.ez_rental.FragmentPages;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ez_rental.Adapter.ReservationsAdapter;
import com.example.ez_rental.Model.Reservation;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.Reservation.ViewReservationActivity;
import com.example.ez_rental.activity.User.UserViewActivity;
import com.example.ez_rental.activity.helper.SQLiteHelper;
import com.example.ez_rental.activity.helper.SessionManager;
import com.example.ez_rental.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ReservationFragment extends Fragment implements ReservationsAdapter.onReservationsListener{
    private SQLiteHelper db;
    private RecyclerView recyclerView;
    private ArrayList<Reservation> list = new ArrayList<>();
    private ArrayList<Reservation> newList = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private ReservationsAdapter adapter;
    private SessionManager session;
    String user_Email = " ";
    ConstraintLayout layout;
    boolean check = true;
    String userrole="";
    public ReservationFragment() {
    }
    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);
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
        if(userrole.contains("user")) {

            view.setOnClickListener(v -> UserViewActivity.hideBottomNav());
            if (user != null) {
                user_Email = user.get("User_ID");
            }
            adapter = loadProducts(view, user_Email);
            adapter.sortNameByAsc();
            swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
            swipeRefreshLayout.setOnRefreshListener(() -> {

                adapter = loadProducts(view, user_Email);
                adapter.sortNameByAsc();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            });

            swipeRefreshLayout.setColorScheme(android.R.color.background_dark,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            setHasOptionsMenu(true);

        }else
        {
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
    private ReservationsAdapter loadProducts(View view,String user_Email) {
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                });
        newList.clear();
        for(int i=0; i<list.size();i++){
            String user_id = String.valueOf(list.get(i).getUser_ID());
            if(user_id.contains(user_Email)){
                newList.add(list.get(i)) ;
            }
        }
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ReservationsAdapter(newList,getContext(), this);
        adapter.sortNameByAsc();
        recyclerView.setAdapter(adapter);

        Volley.newRequestQueue(getContext()).add(stringRequest);

        return adapter;
    }
    @Override
    public void onClick(int position) {

        Intent ReservationInfoPage = new Intent(getActivity(), ViewReservationActivity.class);
        ReservationInfoPage.putExtra("Reservation", newList.get(position));
        startActivity(ReservationInfoPage);

    }


}
