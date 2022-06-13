package com.example.e_hailing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class driverAdapter extends RecyclerView.Adapter<driverAdapter.MyViewHolder> {

    Context context;
    ArrayList<Driver> list;
// TODO : changes

    public driverAdapter(Context context, ArrayList<Driver> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Driver dr = list.get(position);
        holder.Name.setText(dr.getName());
        holder.capacity.setText(String.valueOf(dr.getCapacity()));
        holder.arrivalTime.setText(dr.getArrivedTime());
        holder.reputationTxt.setText(String.valueOf(dr.getRating()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Name, capacity, arrivalTime ,reputationTxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.Name);
            capacity = itemView.findViewById(R.id.cap);
            arrivalTime = itemView.findViewById(R.id.arrivalTime);
            reputationTxt=itemView.findViewById(R.id.reputationTxt);

        }
    }

}