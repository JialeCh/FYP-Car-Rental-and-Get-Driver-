package com.example.ez_rental.FragmentPages;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ez_rental.Adapter.Car_DriverAdapter;
import com.example.ez_rental.Model.Car_Driver;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.MapActivity;
import com.example.ez_rental.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class Car_DriverFragment extends Fragment implements Car_DriverAdapter.onCar_DriverListener{


    private static final String TAG = null;
    private RecyclerView recyclerView;
    private ArrayList<Car_Driver> list = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private Car_DriverAdapter adapter;
    String location = "";
    public Car_DriverFragment() {
        // Required empty public constructor
    }



    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        adapter=loadProducts(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.admin_car_driver, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        adapter=loadProducts(view);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter=loadProducts(view);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });

        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        setHasOptionsMenu(true);

        return view;
    }

    private Car_DriverAdapter loadProducts(View view) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_CD,
                response -> {
                    try {
                        //converting the string to json array object
                        JSONArray array = new JSONArray(response);
                        list.clear();
                        //traversing through all the object
                        for (int i = 0; i < array.length(); i++) {
                            //getting product object from json array
                            JSONObject product = array.getJSONObject(i);
                            //adding the product to product list
                            list.add(new Car_Driver(
                                    product.getInt("CD_ID"),
                                    product.getString("CD_Name"),
                                    product.getString("CD_BOD"),
                                    product.getString("gender"),
                                    product.getString("phone_no"),
                                    product.getString("language_speak"),
                                    product.getInt("CD_LicenseNo"),
                                    product.getString("CD_License_ExpiryDate"),
                                    product.getString("CD_Status"),
                                    product.getString("reason"),
                                    product.getInt("Admin_Id")
                            ));
                        }
                        //creating adapter object and setting it to recyclerview

                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.toString() , Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Log.d(TAG, "Get Response: " + response);
                        Log.d(TAG, "Get Response: " + e.toString());
                    }
                },
                error -> {

                });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Car_DriverAdapter(list, getContext(), this );
        recyclerView.setAdapter(adapter);
        //adding our string request to queue
        Volley.newRequestQueue(getContext()).add(stringRequest);

        return adapter;
    }
    @Override
    public void onClick(int position) {

        Intent CarInfoPage = new Intent(getActivity(), MapActivity.class);
/*        CarInfoPage.putExtra("Car_ID", list.get(position).getCar_Id());
        CarInfoPage.putExtra("Car", list.get(position));
        CarInfoPage.putExtra("Rent_Place", location);*/
        startActivity(CarInfoPage);
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.car_search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

    }



/*        selectVehicleCategory = getArguments().getString("CATEGORY");

        vehicleDao = Room.databaseBuilder(getContext(), Project_Database.class, "car_rental_db").allowMainThreadQueries()
                .build()
                .vehicleDao();


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = (ArrayList<Vehicle>)vehicleDao.getCategoryVehicle(selectVehicleCategory);
        adapter = new VehicleAdapter(getContext(), list,this);
        recyclerView.setAdapter(adapter);*//*



    }

    @Override
    public void onClick(int position) {
        Intent vehicleInfoPage = new Intent(getActivity(), VehicleInfoActivity.class);
        vehicleInfoPage.putExtra("VEHICLE",list.get(position));
        startActivity(vehicleInfoPage);
    }


    //DEBUGING
    private void toast(String txt){
        Toast toast = Toast.makeText(getContext(),txt,Toast.LENGTH_SHORT);
        toast.show();
    }*/
}
