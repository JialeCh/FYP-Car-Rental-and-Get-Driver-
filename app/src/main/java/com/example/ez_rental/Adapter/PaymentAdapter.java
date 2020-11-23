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

import com.example.ez_rental.Model.Payments;
import com.example.ez_rental.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> implements Filterable {


    private PaymentAdapter.onPaymentListener onPaymentListener;
    private Context mCtx;
    private ArrayList<Payments> PaymentList;
    private ArrayList<Payments> PaymentListfull;


    class PaymentViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView payment_id, payment_date, payment_amount;
        Button status;
        PaymentAdapter.onPaymentListener onPaymentListener;
        ConstraintLayout card;
        public PaymentViewHolder(@NonNull View itemView, onPaymentListener onPaymentListener) {
            super(itemView);
            payment_id = itemView.findViewById(R.id.payment_id);
            status=itemView.findViewById(R.id.book);
            payment_date = itemView.findViewById(R.id.py_bod);
            payment_amount = itemView.findViewById(R.id.py_amount);

            this.onPaymentListener =  onPaymentListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onPaymentListener.onClick(getAdapterPosition());
        }
    }
    public Filter getFilter() {
        return PaymentFilter;
    }
    private Filter PaymentFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Payments> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(PaymentListfull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Payments item : PaymentListfull) {
                    if (item.getPayment_ID().toLowerCase().contains(filterPattern)) {
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
            PaymentList.clear();
            PaymentList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };
    public PaymentAdapter(ArrayList<Payments> PaymentsList, Context mCtx, PaymentAdapter.onPaymentListener onPaymentListener) {
        this.onPaymentListener = onPaymentListener;
        this.mCtx = mCtx;
        this.PaymentList = PaymentsList;
        PaymentListfull = new ArrayList<>(PaymentList);
    }
    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.payment_card, null);
        return new PaymentViewHolder(view,onPaymentListener);
    }

    @Override
    public void onBindViewHolder(PaymentViewHolder holder, int position) {
        Payments Payments =  PaymentList.get(position);
        holder.payment_id.setText(PaymentList.get(position).getPayment_ID() +"(Reserve ID:"+Payments.getReserve_ID()+")");
        holder.payment_date.setText("Payment Date :"+PaymentList.get(position).getPayment_Date());
        holder.payment_amount.setText("RM :" +PaymentList.get(position).getPayment_Amount());
        holder.status.setText(PaymentList.get(position).getPayment_Status());
    }
    public void sortNameByAsc() {
        Comparator<Payments> comparator = new Comparator<Payments>() {

            @Override
            public int compare(Payments object1, Payments object2) {
                return object2.getPayment_Date().compareToIgnoreCase(object1.getPayment_Date());
            }
        };
        Collections.sort(PaymentList, comparator);
        notifyDataSetChanged();

    }
    @Override
    public int getItemCount() {
        return PaymentList.size();
    }

    public interface onPaymentListener{
        void onClick(int position);
    }
}