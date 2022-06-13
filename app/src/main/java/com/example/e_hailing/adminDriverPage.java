package com.example.e_hailing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class adminDriverPage extends AppCompatActivity {
    private FloatingActionButton addBtn , removeBtn;
    private TableLayout tableLayout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<Driver> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_driver_page);
        addBtn= findViewById(R.id.addBtn);
        removeBtn=findViewById(R.id.removeBtn);
        tableLayout=findViewById(R.id.tableLayout);
        firebaseDatabase = FirebaseDatabase.getInstance("https://e-hailing-um-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Driver");
        getAllDriver();
        list = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Driver dr=ds.getValue(Driver.class);
                    list.add(dr);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(adminDriverPage.this);
                Context context = adminDriverPage.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);


                final EditText name = new EditText(context);
                name.setHint("Driver Name");
                layout.addView(name); // Notice this is an add method


                final EditText capacity = new EditText(context);
                capacity.setHint("Capacity");
                layout.addView(capacity);

                final EditText currentDestination = new EditText(context);
                currentDestination.setHint("Current Longitude Latitude");

                layout.addView(currentDestination); // Another add method

                final Button submitBtn = new Button(context);
                submitBtn.setText("ADD");
                submitBtn.setTextColor(0xFFF4F1F0);
                submitBtn.setBackgroundColor(0xFF1460E4);
                layout.addView(submitBtn);

                dialog.setView(layout); // Again this is a set method, not add
                AlertDialog alert = dialog.create();

                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!name.getText().toString().equals("")&&!capacity.getText().toString().equals("")&&!capacity.getText().toString().equals("")){
                            Driver driver= new Driver(name.getText().toString(),"Available",currentDestination.getText().toString(),"",Integer.parseInt(capacity.getText().toString()),0);
                            databaseReference.child(name.getText().toString()).setValue(driver);

                            Toast.makeText(adminDriverPage.this, "Driver "+name.getText().toString()+ " added.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context, "Please make sure all the field filled correctly", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


//                submitBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(!name.getText().toString().equals("")&&!capacity.getText().toString().equals("")&&!capacity.getText().toString().equals("")){
//                            if(DB.addDriver(name.getText().toString(),Integer.parseInt(capacity.getText().toString()),currentDestination.getText().toString())){
//                                Toast.makeText(context, "Driver "+name.getText().toString()+ " added.", Toast.LENGTH_SHORT).show();
//                                refresh();
//                                displayInTable();
//                            }else{
//                                Toast.makeText(context, "Driver "+name.getText().toString()+ " already exists.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        else{
//                            Toast.makeText(context, "Please make sure all the field is filled", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

                alert.show();
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(adminDriverPage.this);
                Context context = adminDriverPage.this;
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final TextView title = new TextView(context);
                title.setText("Enter the Driver Name to remove");
                title.setTextSize(21);
                layout.addView(title);

                final EditText name = new EditText(context);
                name.setHint("Driver Name");
                layout.addView(name); // Notice this is an add method



                final Button remove = new Button(context);
                remove.setText("REMOVE");
                remove.setTextColor(0xFFF4F1F0);
                remove.setBackgroundColor(0xFF1460E4);
                layout.addView(remove);

                dialog.setView(layout); // Again this is a set method, not add
                AlertDialog alert = dialog.create();
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!name.getText().toString().equals("")){

                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    if (snapshot.hasChild(name.getText().toString())) {
                                        databaseReference.child(name.getText().toString()).removeValue();
                                        Toast.makeText(context, "Driver "+name.getText().toString()+" removed.", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, "Driver "+name.getText().toString()+" invalid.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        else{
                            Toast.makeText(context, "Please make sure all the Driver Name is filled", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


//                remove.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if(!name.getText().toString().equals("")){
//                            if(DB.deleteDriver(name.getText().toString())){
//                                Toast.makeText(context, "Driver "+name.getText().toString()+ " removed.", Toast.LENGTH_SHORT).show();
//                                if(DB.checkDb()){
//                                    refresh();
//                                    displayInTable();
//                                }
//
//                            }else{
//                                Toast.makeText(context, "Driver "+name.getText().toString()+" invalid.", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        else{
//                            Toast.makeText(context, "Please make sure all the Driver Name is filled", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });

                alert.show();


            }

        });


    }
    public void getAllDriver(){
        DriverList<Driver> res=new DriverList();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refresh();

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    View tableRow = LayoutInflater.from(adminDriverPage.this).inflate(R.layout.table_driver_item,null,false);
                    TextView name  = tableRow.findViewById(R.id.name);
                    TextView status  = tableRow.findViewById(R.id.status);
                    TextView capacity  = tableRow.findViewById(R.id.capacity);
                    TextView currentLongLa  = tableRow.findViewById(R.id.currentLongLa);
                    TextView customer  = tableRow.findViewById(R.id.customer);
                    TextView rating = tableRow.findViewById(R.id.rating);
                    TextView numberOfRating  = tableRow.findViewById(R.id.numberOfRating);

                    Log.d("hjhiuhhiuguigh", "hhhhhhhhhhhhhhhhhhhh ");
                    Driver dr=ds.getValue(Driver.class);

                    name.setText(dr.getName());
                    status.setText(dr.getStatus());
                    capacity.setText(String.valueOf(dr.getCapacity()));
                    currentLongLa.setText(dr.getCurrentLongLa());
                    customer.setText(dr.getCustomer());
                    rating.setText(String.valueOf(dr.getRating()));
                    numberOfRating.setText(String.valueOf(dr.getNumberOfRating()));
                    tableLayout.addView(tableRow);

                    res.addDriver(dr);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(adminDriverPage.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }





    public void refresh(){
        tableLayout.removeAllViews();
    }



}