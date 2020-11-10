package com.example.ez_rental.FragmentPages;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ez_rental.ActivityPages.ViewBookingActivity;
import com.example.ez_rental.Adapter.ReservationsAdapter;
import com.example.ez_rental.Model.Reservation;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;
import com.example.ez_rental.helper.SQLiteHelper;

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
    String user_Email = " ";
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
        db = new SQLiteHelper(getContext());
        HashMap<String, String> user = db.getUserDetails();

        if (user != null) {
            user_Email = user.get("User_ID");
        }
        adapter=loadProducts(view,user_Email);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter=loadProducts(view,user_Email);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });

        swipeRefreshLayout.setColorScheme(android.R.color.background_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        setHasOptionsMenu(true);
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
        recyclerView.setAdapter(adapter);

        Volley.newRequestQueue(getContext()).add(stringRequest);

        return adapter;
    }
    @Override
    public void onClick(int position) {

        Intent CarInfoPage = new Intent(getActivity(), ViewBookingActivity.class);
        CarInfoPage.putExtra("Reservation", newList.get(position));

        startActivity(CarInfoPage);

    }

}
