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

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    ArrayList<User> list;

    public MyAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View v= LayoutInflater.from(context).inflate(R.layout.userentry,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user=list.get(position);
     //   holder.donarname.setText(user.);
//        holder.phoneNo.setText(user.getdPhoneNo());
//        holder.address.setText(user.getAddrdess());
//        holder.donationType.setText(user.getDdonationType());
//        holder.cookedBefore.setText(user.getCookeddBefore());
//        holder.chooseTypeOfPlace.setText(user.getTypdeOfPlace());
//        holder.status.setText(user.getSdtatus());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView donarname,phoneNo,address,donationType,cookedBefore,chooseTypeOfPlace,status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            donarname=itemView.findViewById(R.id.txtDonarName);
            phoneNo=itemView.findViewById(R.id.txtPhoneNo);
            address=itemView.findViewById(R.id.txtAddress);
            donationType=itemView.findViewById(R.id.txtDonationType);
            cookedBefore=itemView.findViewById(R.id.txtCookedBefore);
            chooseTypeOfPlace=itemView.findViewById(R.id.txtPlaceType);
            status=itemView.findViewById(R.id.txtStatus);
        }
    }
}
