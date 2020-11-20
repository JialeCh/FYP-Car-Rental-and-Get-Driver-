package com.example.ez_rental.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ez_rental.Model.Brand;
import com.example.ez_rental.R;

import java.util.ArrayList;


public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder> {


    private BrandAdapter.onBrandListener onBrandListener;

    private Context mCtx;
    private ArrayList<Brand> ReservationList;
    private ArrayList<Brand> ReservationListfull;




    class BrandViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView brandID,branddesc,brandName,textView8;

        BrandAdapter.onBrandListener onBrandListener;
        ConstraintLayout card;
        public BrandViewHolder(@NonNull View itemView, onBrandListener onBrandListener) {
            super(itemView);
            brandID = itemView.findViewById(R.id.brandID);
            textView8= itemView.findViewById(R.id.textView8);
            branddesc = itemView.findViewById(R.id.branddesc);
            brandName = itemView.findViewById(R.id.brandName);
            this.onBrandListener =  onBrandListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onBrandListener.onClick(getAdapterPosition());
        }
    }

    public BrandAdapter(ArrayList<Brand> carsList, Context mCtx, BrandAdapter.onBrandListener onReservationsListener) {
        this.onBrandListener = onReservationsListener;
        this.mCtx = mCtx;
        this.ReservationList = carsList;
        ReservationListfull = new ArrayList<>(ReservationList);
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.brand_card, null);
        return new BrandViewHolder(view,onBrandListener);
    }





    @Override
    public void onBindViewHolder(BrandViewHolder holder, int position) {
        Brand brand=  ReservationList.get(position);
        holder.branddesc.setText("Brand Description : "+brand.getDescription());
        holder.brandID.setText("Brand ID : "+brand.getBrand_Id());
        holder.brandName.setText("Brand Name : "+brand.getBrand_Name());
        holder.textView8.setText(brand.getBrand_status());
    }

    @Override
    public int getItemCount() {
        return ReservationList.size();
    }

    public interface onBrandListener{
        void onClick(int position);
    }
}