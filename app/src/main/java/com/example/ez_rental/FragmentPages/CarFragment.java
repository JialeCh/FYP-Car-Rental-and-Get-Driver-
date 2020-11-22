package com.example.ez_rental.FragmentPages;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ez_rental.Adapter.CarsAdapter;
import com.example.ez_rental.Model.Car;
import com.example.ez_rental.Model.Feedback;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.Reservation.CarActivity;
import com.example.ez_rental.activity.helper.HttpParse;
import com.example.ez_rental.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;


public class CarFragment extends Fragment implements CarsAdapter.onCarsListener{
    


    private RecyclerView recyclerView;
    private ArrayList<Car> list = new ArrayList<>();
    private ArrayList<Car> newlist = new ArrayList<>();
    private ArrayList<Feedback> list2 = new ArrayList<>();
    private ArrayList<Feedback> newlist2 = new ArrayList<>();
    private double totalRate;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    SwipeRefreshLayout swipeRefreshLayout;
    private CarsAdapter adapter;
    private boolean check = true;
    private TextView txtResult,show;
    private ImageView btnBack,btnList;
    private Spinner btnSort;
    private int count = 0;
    public CarFragment() {

    }



    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        init(view);
        adapter=loadProducts(view);
        checkAdapter(adapter);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter=loadProducts(view);
            checkAdapter(adapter);
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

    private void init(View view){
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setVisibility(View.GONE);
        txtResult = view.findViewById(R.id.textView2);
        btnSort = view.findViewById(R.id.btnSort);
        btnList =view.findViewById(R.id.imageView17);
        show = view.findViewById(R.id.choose);
        btnList.setOnClickListener(v->{
            if(check){
                btnList.setImageDrawable(getResources().getDrawable(R.drawable.ic_down));
                toast("Descending Order");
                check = false;
            }else{
                btnList.setImageDrawable(getResources().getDrawable(R.drawable.ic_up));
                toast("Ascending Order");
                check = true;
            }
        });

        List<String> categories = new ArrayList<String>();
        categories.add(" ");
        categories.add("Car Name");
        categories.add("Car Rating");
        categories.add("Car Brand");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        btnSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String value= btnSort.getSelectedItem().toString();

                if (value.contains("Name")){
                    if(check){
                        toast("Car List (Sort by Name :ASC)");
                        adapter.sortNameByAsc();
                    }else{
                        toast("Car List (Sort by Name :DESC)");
                        adapter.sortNameByDesc();
                    }
                }else if (value.contains("Brand")){
                    if(check){
                        toast("Car List (Sort by Brand :ASC)");
                        adapter.sortBrandByAsc();
                    }else{
                        toast("Car List (Sort by Brand :DESC)");
                        adapter.sortBrandByDesc();
                    }
                }else if (value.contains("Rating")) {
                    if (check) {
                        toast("Car List (Sort by Rating :ASC)");
                        adapter.sortRateByAsc();
                    } else {
                        toast("Car List (Sort by Rating :DESC)");
                        adapter.sortRateByDesc();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                toast("Nothing Selected");
            }
        });
        btnSort.setAdapter(dataAdapter);
    }
    private void checkAdapter(CarsAdapter adapters){
        if(adapters == null){
            txtResult.setVisibility(View.VISIBLE);
        }
        else {
            txtResult.setVisibility(View.INVISIBLE);
        }
    }
    private CarsAdapter loadProducts(View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_Cars,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        list.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject product = array.getJSONObject(i);
                            list.add(new Car(
                                    product.getInt("Car_Id"),
                                    product.getString("Car_Title"),
                                    product.getString("Car_Overview"),
                                    product.getDouble("PricePerDay"),
                                    product.getString("Fuel_Type"),
                                    product.getInt("ModelYear"),
                                    product.getInt("Seating_Cap"),
                                    product.getString("VImage1"),
                                    product.getString("VImage2"),
                                    product.getString("VImage3"),
                                    product.getString("RegDate"),
                                    product.getString("UpdationDate"),
                                    product.getString("Car_Status"),
                                    product.getInt("Admin_Id"),
                                    product.getInt("Brand_Id"),
                                    product.getString("Brand_Name"),
                                    product.getString("Location"),
                                    product.getString("plate_no"),
                                    product.getString("color"),
                                    product.getInt("rating"),
                                    product.getString("reason")
                            ));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                });
        newlist.clear();
        for(int i=0; i<list.size();i++){
            newlist.add(list.get(i));

            totalRate= loadRate(String.valueOf(newlist.get(i).getCar_Id()));
            Log.e(TAG, "outside" + totalRate );
        }
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CarsAdapter( newlist, getContext(), this);
        recyclerView.setAdapter(adapter);

        Volley.newRequestQueue(getContext()).add(stringRequest);

        return adapter;
    }
    @Override
    public void onClick(int position) {

        Intent CarInfoPage = new Intent(getActivity(), CarActivity.class);
        CarInfoPage.putExtra("Car", newlist.get(position));
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
    private void toast(String txt){
        Toast toast = Toast.makeText(getContext(),txt,Toast.LENGTH_LONG);
        toast.show();
    }
    private double loadRate(String car_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_feedback,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        list.clear();
                        totalRate = 0;
                        count=0;
                        for (int i = 0; i < array.length(); i++) {

                            JSONObject product = array.getJSONObject(i);
                            list2.add(new Feedback(
                                    product.getInt("Feedback_ID"),
                                    product.getInt("Reserve_Id"),
                                    product.getInt("Car_Id"),
                                    product.getString("User_Feedback"),
                                    product.getInt("User_Rating"),
                                    product.getString("Feedback_Date"),
                                    product.getInt("User_ID"),
                                    product.getInt("Admin_Id")

                            ));
                            newlist2.clear();

                            if(String.valueOf(list2.get(i).getCar_Id()).compareTo(car_id) == 0){
                                newlist2.add(list2.get(i)) ;
                                totalRate = totalRate + list2.get(i).getUser_Rating();
                                count= count + 1;


                            }
                            double finaltotalRate = totalRate / count;
                            if(newlist2.size() > 0){
                             carRateUpdate(car_id,finaltotalRate);
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        e.printStackTrace();
                    }
                },
                error -> {
                });

        Volley.newRequestQueue(getContext()).add(stringRequest);
       return totalRate;
    }
    public void carRateUpdate( final String Car_Id, final double rating) {

        class carRateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                totalRate = 0;
            }
            @Override
            protected String doInBackground(String... params) {
                hashMap.put("Car_Id", params[0]);
                hashMap.put("rating", params[1]);

                finalResult = httpParse.postRequest(hashMap, AppConfig.HttpUpdateRating);

                return finalResult;
            }
        }
        carRateClass UpdateClass = new  carRateClass();
        UpdateClass.execute(Car_Id,String.valueOf(rating));
    }

}
