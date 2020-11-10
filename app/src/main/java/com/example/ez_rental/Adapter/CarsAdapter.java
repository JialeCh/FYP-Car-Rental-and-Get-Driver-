package com.example.ez_rental.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ez_rental.Model.Car;
import com.example.ez_rental.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.CarsViewHolder> implements Filterable {


    private CarsAdapter.onCarsListener onCarsListener;
    private Context mCtx;
    private ArrayList<Car> CarsList;
    private ArrayList<Car> CarsListfull;


    class CarsViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView vehicle, detail, price,rate;
        ImageView vehicleImage;
        ConstraintLayout card;
        Button btnStatus;
        CarsAdapter.onCarsListener onCarsListener;
        public CarsViewHolder(@NonNull View itemView, onCarsListener onCarsListener) {
            super(itemView);
            vehicle = itemView.findViewById(R.id.vehicle);
            detail = itemView.findViewById(R.id.detail);
            card = itemView.findViewById(R.id.card);
            price = itemView.findViewById(R.id.price);
            rate = itemView.findViewById(R.id.rateCar);
            btnStatus = itemView.findViewById(R.id.book);
            vehicleImage = itemView.findViewById(R.id.vehicleImage);
            this. onCarsListener =  onCarsListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCarsListener.onClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public CarsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.vehicle_card, null);
        return new CarsViewHolder(view,onCarsListener);
    }

    @Override
    public Filter getFilter() {
        return carFilter;
    }
    private Filter carFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Car> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(CarsListfull);
            } else {
                String filterPattern = constraint.toString().toLowerCase();
                for (Car item : CarsListfull) {
                    if (item.getCar_Title().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            CarsList.clear();
            CarsList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
    public CarsAdapter(ArrayList<Car> carsList, Context mCtx, onCarsListener onCarsListener) {
        this.onCarsListener = onCarsListener;
        this.mCtx = mCtx;
        this.CarsList = carsList;
        CarsListfull = new ArrayList<>(CarsList);
    }

    public void sortNameByAsc() {
        Comparator<Car> comparator = new Comparator<Car>() {

            @Override
            public int compare(Car object1, Car object2) {
                return object1.getCar_Title().compareToIgnoreCase(object2.getCar_Title());
            }
        };
        Collections.sort(CarsList, comparator);
        notifyDataSetChanged();

    }
    @Override
    public void onBindViewHolder(CarsViewHolder holder, int position) {
        Car Cars =  CarsList.get(position);
        holder.rate.setText(String.valueOf(Cars.getRating()));
        holder.vehicle.setText(Cars.getCar_Title() + "( "+ Cars.getBrand_Name() +" )");
        holder.price.setText("RM "+Cars.getPricePerDay()+"/day");
        Picasso.get().load(Cars.getVImage1()).into(holder.vehicleImage);
        if (Cars.getCar_Status().contains("Good")){
            holder.btnStatus.setText("Available");
        }
        else{
            holder.btnStatus.setText("Unavailable");
        }

    }

    @Override
    public int getItemCount() {
        return CarsList.size();
    }

    public interface onCarsListener{
        void onClick(int position);
    }
}