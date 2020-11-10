package com.example.ez_rental.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ez_rental.Adapter.Car_DriverAdapter;
import com.example.ez_rental.Model.Car_Driver;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class GetCarDriverDialog extends AppCompatDialogFragment {

    private ArrayList<Car_Driver> list = new ArrayList<>();
    private RecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;
    private Car_DriverAdapter adapter;

    private GetCarDriverListener listener;
    String driveName;
    private ArrayList<Car_Driver> Car_Driver = new ArrayList<>();
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_car_driver, null);

        builder.setView(view)
                .setTitle("Available Car Driver")
                .setNegativeButton("cancel", (dialogInterface, i) -> {
                })
                .setPositiveButton("ok", (dialogInterface, i) -> listener.applyTexts(driveName));
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

        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (GetCarDriverListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }
    public interface GetCarDriverListener {
        void applyTexts(String username);
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

        adapter = new Car_DriverAdapter(list, getContext(), this );
        recyclerView.setAdapter(adapter);
        //adding our string request to queue
        Volley.newRequestQueue(getContext()).add(stringRequest);

        return adapter;
    }


}

