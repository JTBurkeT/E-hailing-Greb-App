package com.example.e_hailing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class customerAdapter extends RecyclerView.Adapter<customerAdapter.MyViewHolder> {

    Context context;
    ArrayList<CustomerObject> list;
// TODO : changes

    public customerAdapter(Context context, ArrayList<CustomerObject> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.customer_card_view,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CustomerObject dr = list.get(position);
        holder.Name.setText(dr.getUserName());
        holder.capacity.setText(String.valueOf(dr.getCapacity()));
        holder.arrivalTime.setText(dr.getExpectedArrivalTime());
        holder.status.setText(dr.getStatus());
        holder.starting.setText(dr.getStartingLongLa());
        holder.ending.setText(dr.getDestinationLongLa());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Name, capacity, arrivalTime,status,starting,ending;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.Name);
            capacity = itemView.findViewById(R.id.cap);
            arrivalTime = itemView.findViewById(R.id.arrivalTime);
            status =itemView.findViewById(R.id.status);
            starting=itemView.findViewById(R.id.starting);
            ending=itemView.findViewById(R.id.ending);

        }
    }

}
