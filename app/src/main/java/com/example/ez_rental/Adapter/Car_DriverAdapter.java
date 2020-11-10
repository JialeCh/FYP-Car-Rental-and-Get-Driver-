package com.example.ez_rental.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ez_rental.Model.Car_Driver;
import com.example.ez_rental.R;
import com.example.ez_rental.helper.GetCarDriverDialog;

import java.util.ArrayList;


public class Car_DriverAdapter extends RecyclerView.Adapter<Car_DriverAdapter.Car_DriverViewHolder> implements Filterable {


    private Car_DriverAdapter.onCar_DriverListener onCar_DriverListener;
    private GetCarDriverDialog getCarDriverDialog;
    private Context mCtx;
    private ArrayList<Car_Driver> ReservationList;
    private ArrayList<Car_Driver> ReservationListfull;

    public Car_DriverAdapter(ArrayList<Car_Driver> list, Context context, GetCarDriverDialog getCarDriverDialog) {
        this.getCarDriverDialog = getCarDriverDialog;
        this.mCtx = context;
        this.ReservationList = list;
        ReservationListfull = new ArrayList<>(ReservationList);
    }


    class Car_DriverViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView driverName,birthdate, language;
        Button driverStatus;
        Car_DriverAdapter.onCar_DriverListener onCar_DriverListener;
        ConstraintLayout card;
        public Car_DriverViewHolder(@NonNull View itemView, onCar_DriverListener onCar_DriverListener) {
            super(itemView);
            driverName = itemView.findViewById(R.id.Driver_Name);

            birthdate = itemView.findViewById(R.id.cd_bod);
            driverStatus = itemView.findViewById(R.id.cd_status);
            language = itemView.findViewById(R.id.Status);
            this.onCar_DriverListener =  onCar_DriverListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCar_DriverListener.onClick(getAdapterPosition());
        }
    }
    public Filter getFilter() {
        return Car_DriverFilter;
    }
    private Filter Car_DriverFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Car_Driver> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(ReservationListfull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Car_Driver item : ReservationListfull) {
                    if (item.getCD_Name().toLowerCase().contains(filterPattern)) {
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
            ReservationList.clear();
            ReservationList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
    public Car_DriverAdapter(ArrayList<Car_Driver> carsList, Context mCtx, Car_DriverAdapter.onCar_DriverListener onReservationsListener) {
        this.onCar_DriverListener = onReservationsListener;
        this.mCtx = mCtx;
        this.ReservationList = carsList;
        ReservationListfull = new ArrayList<>(ReservationList);
    }

    @NonNull
    @Override
    public Car_DriverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_dialog, null);
        return new Car_DriverViewHolder(view,onCar_DriverListener);
    }



    public ArrayList<Car_Driver> filterlocation (ArrayList<Car_Driver> carsList, String status){
        ArrayList<Car_Driver> newCarList = new ArrayList<>();
        for (Car_Driver item : carsList) {

            if (String.valueOf(item.getCD_Status()).toLowerCase().contains(status) ){
                newCarList.add(item);
            }
        }
        return newCarList;
    }


    @Override
    public void onBindViewHolder(Car_DriverViewHolder holder, int position) {
        Car_Driver Reservations =  ReservationList.get(position);
        holder.driverName.setText(Reservations.getCD_Name());
        holder.birthdate.setText("Birth Date :"+Reservations.getCD_BOD());
        holder.driverStatus.setText("Status :" +Reservations.getCD_Status());
        holder.language.setText("Language Speak: "+Reservations.getLanguage_speak());
    }

    @Override
    public int getItemCount() {
        return ReservationList.size();
    }

    public interface onCar_DriverListener{
        void onClick(int position);
    }
}