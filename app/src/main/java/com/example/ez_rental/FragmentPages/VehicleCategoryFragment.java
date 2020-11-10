package com.example.ez_rental.FragmentPages;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ez_rental.Adapter.VehicleCategoryAdapter;
import com.example.ez_rental.Database.VehicleCategoryDao;
import com.example.ez_rental.Model.VehicleCategory;
import com.example.ez_rental.R;

import java.util.ArrayList;

/*import com.example.carrentalapp.Database.Project_Database;*/


public class VehicleCategoryFragment extends Fragment implements VehicleCategoryAdapter.onCategoryListener {

    private VehicleCategoryDao vehicleCategoryDao;

    private RecyclerView recyclerView;
    private VehicleCategoryAdapter adapter;

    private Button home;

    private ArrayList<VehicleCategory> list;

    public VehicleCategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        View view = inflater.inflate(R.layout.fragment_vehicle_category, container, false);
        
        initComponents(view);
        listenHandler();

        return view;
    }

    private void initComponents(View view) {

/*        vehicleCategoryDao = Room.databaseBuilder(getContext(), Project_Database.class, "car_rental_db").allowMainThreadQueries()
                .build()
                .vehicleCategoryDao();*/

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = (ArrayList<VehicleCategory>) vehicleCategoryDao.getAllCategory();
        adapter = new VehicleCategoryAdapter(getContext(), list,this);
        recyclerView.setAdapter(adapter);
    }

    private void listenHandler() {

//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent homePage = new Intent(HomePageActivity.this, LoginActivity.class);
//                startActivity(homePage);
//            }
//        });


    }

    //DEBUGING
    private void toast(String txt) {
        Toast toast = Toast.makeText(getContext(), txt, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onCategoryClick(int position) {
        toast(list.get(position).getCategory());

        String selectedCategory = list.get(position).getCategory();

        Bundle bundle=new Bundle();
        bundle.putString("CATEGORY", selectedCategory);

        Fragment viewVehicle = new CarFragment();
        viewVehicle.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, viewVehicle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onSelectClick(int position) {
        toast(list.get(position).getCategory() + " Select");
    }

}
