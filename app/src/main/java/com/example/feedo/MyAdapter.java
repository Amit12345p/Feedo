package com.example.feedo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myviewholder> {
    ArrayList<User>list;

    public MyAdapter(ArrayList<User> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.userentry,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {
        TextView txtDonarName, txtPhoneNo, txtAddress, txtDonationType, txtCookedBefore, txtPlaceType, txtStatus;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            txtDonarName = itemView.findViewById(R.id.txtDonarName);
            txtPhoneNo = itemView.findViewById(R.id.txtPhoneNo);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtDonationType = itemView.findViewById(R.id.txtDonationType);
            txtCookedBefore = itemView.findViewById(R.id.txtCookedBefore);
            txtPlaceType = itemView.findViewById(R.id.txtPlaceType);
            txtStatus = itemView.findViewById(R.id.txtStatus);

        }
    }
}