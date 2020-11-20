package com.example.ez_rental.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ez_rental.Model.Feedback;
import com.example.ez_rental.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {


    private RatingAdapter.onRatingListener onRatingListener;
    private Context mCtx;
    private ArrayList<Feedback> FeedbackList;



    class RatingViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView  rateCar, FeedbackTxtx,user_Id,detail;

        ImageView feebbackImg;
        ConstraintLayout card;

        RatingAdapter.onRatingListener onRatingListener;
        public RatingViewHolder(@NonNull View itemView, onRatingListener onRatingListener) {
            super(itemView);

            rateCar = itemView.findViewById(R.id.rateCar);
            detail = itemView.findViewById(R.id.detail);
            card = itemView.findViewById(R.id.card);
            FeedbackTxtx = itemView.findViewById(R.id.FeedbackTxtx);
            user_Id = itemView.findViewById(R.id.user_Id);

            feebbackImg = itemView.findViewById(R.id.feebbackImg);
            this. onRatingListener =  onRatingListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onRatingListener.onClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.ratingcard, null);
        return new RatingViewHolder(view,onRatingListener);
    }


    public RatingAdapter(ArrayList<Feedback> FeedbackList, Context mCtx, onRatingListener onRatingListener) {
        this.onRatingListener = onRatingListener;
        this.mCtx = mCtx;
        this.FeedbackList = FeedbackList;
        notifyDataSetChanged();
    }

    public RatingAdapter(ArrayList<Feedback> FeedbackList, Context mCtx) {
        this.onRatingListener = onRatingListener;
        this.mCtx = mCtx;
        this.FeedbackList = FeedbackList;
        notifyDataSetChanged();
    }

    public void sortDateByAsc() {
        Comparator<Feedback> comparator = new Comparator<Feedback>() {

            @Override
            public int compare(Feedback object1, Feedback object2) {
                return object1.getFeedback_Date().compareToIgnoreCase(object2.getFeedback_Date());
            }
        };
        Collections.sort(FeedbackList, comparator);
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(RatingViewHolder holder, int position) {

        Feedback feedback =  FeedbackList.get(position);

        holder.user_Id.setText("User ID:"+feedback.getUser_ID() + "(Car ID: "+feedback.getCar_Id() +")");
        if(feedback.getUser_Feedback().compareTo("")== 0){
            holder.FeedbackTxtx.setText("No comment");
        }else
        {
            holder.FeedbackTxtx.setText(feedback.getUser_Feedback());
        }

        holder.rateCar.setText(" "+feedback.getUser_Rating()+".0");

        holder.detail.setText(feedback.getFeedback_Date());

        switch(feedback.getUser_Rating()){
            case 1:
            case 2:
                holder.feebbackImg.setImageResource(R.drawable.emoji_crying) ;break;
            case 3:
            case 4:
                holder.feebbackImg.setImageResource(R.drawable.emoji_angry) ;break;
            case 5:
            case 6:
                holder.feebbackImg.setImageResource(R.drawable.emoji_thinking) ;break;
            case 7:
            case 8:
                holder.feebbackImg.setImageResource(R.drawable.emoji_happy) ;break;
            case 9:
            case 10:
                holder.feebbackImg.setImageResource(R.drawable.emoji_in_love) ;break;
            default:
                holder.feebbackImg.setImageResource(R.drawable.ic_star) ;break;

        }

    }

    @Override
    public int getItemCount() {
        return FeedbackList.size();
    }

    public interface onRatingListener{
        void onClick(int position);
    }


}