package com.example.e_hailing;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminCustomerPage extends AppCompatActivity {
    private TableLayout tableLayout;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_customer_page);
        tableLayout=findViewById(R.id.tableLayout);
        firebaseDatabase = FirebaseDatabase.getInstance("-");
        databaseReference = firebaseDatabase.getReference("Customer");
        getAllCustomer();
    }

    public void getAllCustomer(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refresh();

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    View tableRow = LayoutInflater.from(adminCustomerPage.this).inflate(R.layout.table_item,null,false);
                    TextView name  = tableRow.findViewById(R.id.name);
                    TextView status  = tableRow.findViewById(R.id.status);
                    TextView expectedArrivalTime = tableRow.findViewById(R.id.expectedArrivalTime);
                    TextView capacity  = tableRow.findViewById(R.id.capacity);
                    TextView startLongLa  = tableRow.findViewById(R.id.startLongLa);
                    TextView destination  = tableRow.findViewById(R.id.destinationLongLa);

                    CustomerObject cs= ds.getValue(CustomerObject.class);

                    name.setText(cs.getUserName());
                    status.setText(cs.getStatus());
                    expectedArrivalTime.setText(cs.getExpectedArrivalTime());
                    capacity.setText(String.valueOf(cs.getCapacity()));
                    startLongLa.setText(cs.getStartingLongLa());
                    destination.setText(cs.getDestinationLongLa());
                    tableLayout.addView(tableRow);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(adminCustomerPage.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void refresh(){
        tableLayout.removeAllViews();
    }

}