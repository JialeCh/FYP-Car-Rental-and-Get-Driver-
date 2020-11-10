package com.example.ez_rental.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ez_rental.Model.Reservation;
import com.example.ez_rental.R;
import com.example.ez_rental.helper.SQLiteHelper;

import java.util.ArrayList;


public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationsViewHolder> implements Filterable {

    private SQLiteHelper db;
    private ReservationsAdapter.onReservationsListener onReservationsListener;
    private Context mCtx;
    private ArrayList<Reservation> ReservationList;
    private ArrayList<Reservation> ReservationListfull;



    class ReservationsViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView vehicleName, bookingID, customerName,
                pickupDate, returnDate,ReservationStatus;
        ReservationsAdapter.onReservationsListener onReservationsListener;
        ConstraintLayout card;

        public ReservationsViewHolder(@NonNull View itemView, onReservationsListener onReservationsListener) {
            super(itemView);
            vehicleName = itemView.findViewById(R.id.vehicleName);

            bookingID = itemView.findViewById(R.id.bookingID);
            customerName = itemView.findViewById(R.id.customerName);
            pickupDate = itemView.findViewById(R.id.pickupDate);
            returnDate = itemView.findViewById(R.id.returnDate);
            ReservationStatus = itemView.findViewById(R.id.bookingStatus);
            this.onReservationsListener =  onReservationsListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onReservationsListener.onClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ReservationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.booking_card, null);
        return new ReservationsViewHolder(view,onReservationsListener);
    }

    @Override
    public Filter getFilter() {
        return carFilter;
    }
    private Filter carFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Reservation> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(ReservationListfull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Reservation item : ReservationListfull) {
                    if (item.getInsurance().toLowerCase().contains(filterPattern)) {
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
    public ReservationsAdapter(ArrayList<Reservation> carsList, Context mCtx, onReservationsListener onReservationsListener) {
        this.onReservationsListener = onReservationsListener;
        this.mCtx = mCtx;
        this.ReservationList = carsList;
        ReservationListfull =  new ArrayList<>(ReservationList);
    }



    @Override
    public void onBindViewHolder(ReservationsViewHolder holder, int position) {
        Reservation Reservations =  ReservationList.get(position);

        holder.vehicleName.setText(Reservations.getCar_Name()+ " ( Car ID: "+ Reservations.getCar_Id()+" )");
        holder.bookingID.setText(Reservations.getReserve_ID()+ " ");
        holder.customerName.setText(Reservations.getUser_ID() + " ");
        holder.pickupDate.setText(Reservations.getReserve_Date() + " ");
        holder.returnDate.setText(Reservations.getReturn_Date() + " ");
        holder.ReservationStatus.setText(Reservations.getStatus() + " ");

    }

    @Override
    public int getItemCount() {
        return ReservationList.size();
    }

    public interface onReservationsListener{
        void onClick(int position);
    }
}