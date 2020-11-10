package com.example.ez_rental.FragmentPages;


import android.content.Intent;
import android.os.Bundle;
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
import com.example.ez_rental.ActivityPages.CarInfoActivity;
import com.example.ez_rental.Adapter.CarsAdapter;
import com.example.ez_rental.Model.Car;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class CarFragment extends Fragment implements CarsAdapter.onCarsListener{
    


    private RecyclerView recyclerView;
    private ArrayList<Car> list = new ArrayList<>();
    private ArrayList<Car> newlist = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private CarsAdapter adapter;
    String location = "";
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
         Bundle bundle = getArguments();
         location = bundle.getString("location");

        Toast.makeText(getContext(), location, Toast.LENGTH_SHORT).show();
        adapter=loadProducts(view,location);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter=loadProducts(view,location);
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

    private CarsAdapter loadProducts(View view, String location) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_Cars,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        list.clear();
                        newlist.clear();
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
        for(int i=0; i<list.size();i++){
            String locate = String.valueOf(list.get(i).getLocation());
            if(locate .contains(location)){
                newlist.add(list.get(i)) ;
            }
        }
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CarsAdapter( newlist, getContext(), this);
        recyclerView.setAdapter(adapter);
        //adding our string request to queue
        Volley.newRequestQueue(getContext()).add(stringRequest);

        return adapter;
    }
    @Override
    public void onClick(int position) {

        Intent CarInfoPage = new Intent(getActivity(), CarInfoActivity.class);
        CarInfoPage.putExtra("Car", newlist.get(position));
        CarInfoPage.putExtra("Rent_Place", location);
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
         /*       adapter.getFilter().filter(newText);*/
                return false;
            }
        });

    }



}
