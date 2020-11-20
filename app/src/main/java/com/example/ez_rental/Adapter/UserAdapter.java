package com.example.ez_rental.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ez_rental.Model.User;
import com.example.ez_rental.R;

import java.util.ArrayList;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {


    private UserAdapter.onUserListener onUserListener;

    private Context mCtx;
    private ArrayList<User> UserList;
    private ArrayList<User> UserListfull;




    class UserViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView User_Id, phone,email,address, license;
        Button book;
        UserAdapter.onUserListener onUserListener;
        ConstraintLayout card;
        public UserViewHolder(@NonNull View itemView, onUserListener onUserListener) {
            super(itemView);
            User_Id = itemView.findViewById(R.id.User_Id);
            phone= itemView.findViewById(R.id.user_Id4);
            email= itemView.findViewById(R.id.user_Id3);
            address = itemView.findViewById(R.id.user_Id2);
            license = itemView.findViewById(R.id.user_Id5);
            book = itemView.findViewById(R.id.book);
            this.onUserListener =  onUserListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onUserListener.onClick(getAdapterPosition());
        }
    }

    public UserAdapter(ArrayList<User> carsList, Context mCtx, UserAdapter.onUserListener onUserListener) {
        this.onUserListener = onUserListener;
        this.mCtx = mCtx;
        this.UserList = carsList;
        UserListfull = new ArrayList<>(UserList);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.user_card, null);
        return new UserViewHolder(view,onUserListener);
    }





    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user=  UserList.get(position);
        holder.address.setText("Address:"+user.getAddress());
        holder.User_Id.setText("User ID:"+user.getUser_ID()+"("+user.getUsername()+")");
        holder.license.setText("License No:"+user.getDriver_license()+"("+user.getLicense_ExpiryDate()+")");

        if(user.getUser_Password().contains("Deactivate")){
            holder.book.setText("Deactivate");
        }else{
            holder.book.setText("Active");
        }
        holder.phone.setText("Phone:"+user.getUser_ContactNo());
        holder.email.setText("Email:"+user.getUser_Email());

    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }

    public interface onUserListener{
        void onClick(int position);
    }
}