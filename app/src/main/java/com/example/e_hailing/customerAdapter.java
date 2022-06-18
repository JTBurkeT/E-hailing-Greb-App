package com.example.e_hailing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
This class is to perform as the adapter to adapt the driver object from the list and put it inside the recycler view
 */
public class customerAdapter extends RecyclerView.Adapter<customerAdapter.MyViewHolder> {

    Context context;
    ArrayList<CustomerObject> list;

    //The constructor of the customerAdapter
    public customerAdapter(Context context, ArrayList<CustomerObject> list) {
        this.context = context;
        this.list = list;
    }

    //Set the viewHolder which is the card view
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.customer_card_view,parent,false);
        return  new MyViewHolder(v);
    }


    //Bind the data of the customer object with the customer card view
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

    //to get the size of the list
    @Override
    public int getItemCount() {
        return list.size();
    }


    // use to initialize the widget in the customer card view
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
