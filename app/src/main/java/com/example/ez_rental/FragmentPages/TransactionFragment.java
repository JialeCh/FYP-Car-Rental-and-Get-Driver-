package com.example.ez_rental.FragmentPages;

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
import com.example.ez_rental.Adapter.PaymentAdapter;
import com.example.ez_rental.Model.Payments;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;
import com.example.ez_rental.helper.SQLiteHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionFragment extends Fragment implements PaymentAdapter.onPaymentListener {

    private static final String TAG = null;
    private RecyclerView recyclerView;
    private ArrayList<Payments> list = new ArrayList<>();
    private ArrayList<Payments> newlist = new ArrayList<>();
    private   SwipeRefreshLayout swipeRefreshLayout;
    private PaymentAdapter adapter;
    private  String userid;
    private SQLiteHelper db;
    public TransactionFragment() {
        // Required empty public constructor
    }
    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transaction_fragment, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        db = new SQLiteHelper(getContext());

        HashMap<String, String> user = db.getUserDetails();
        userid = user.get("User_ID");
        adapter=loadProducts(view,userid);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {

            adapter=loadProducts(view,userid);
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

    private PaymentAdapter loadProducts(View view, String uid) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_payment,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        list.clear();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject product = array.getJSONObject(i);
                            list.add(new Payments(
                                    product.getString("Payment_ID"),
                                    product.getDouble("Payment_Amount"),
                                    product.getString("Payment_Date"),
                                    product.getString("Payment_Method"),
                                    product.getString("Payment_Status"),
                                    product.getInt("Card_No"),
                                    product.getInt("Reserve_ID"),
                                    product.getInt("User_ID")));
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        Log.d(TAG, "Get Response: " + response);
                        Log.d(TAG, "Get Response: " + e.toString());
                    }
                },
                error -> {

                });
        newlist.clear();
        for (int i = 0; i < list.size(); i++) {

            String ID = String.valueOf(list.get(i).getUser_ID());
            if (ID.contains(uid)) {
                newlist.add(list.get(i));
            }
        }
            recyclerView = view.findViewById(R.id.recyclerViewPY);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new PaymentAdapter(newlist,getContext(), this);
            recyclerView.setAdapter(adapter);
            Volley.newRequestQueue(getContext()).add(stringRequest);
        return adapter;
        }


    @Override
    public void onClick(int position) {

    /*    Intent CarInfoPage = new Intent(getActivity(), MapActivity.class);
        startActivity(CarInfoPage);*/
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
}
