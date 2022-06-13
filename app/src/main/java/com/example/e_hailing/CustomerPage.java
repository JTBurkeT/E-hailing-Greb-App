package com.example.e_hailing;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
//class Set{
//    //store the boolean and time when receiving the result from the requested API
//    private Boolean check;
//    private int time;
//    public Set(Boolean check, int time){
//        this.time=time;
//        this.check=check;
//    }
//
//    public Boolean getCheck() {
//        return check;
//    }
//
//    public void setCheck(Boolean check) {
//        this.check = check;
//    }
//
//    public int getTime() {
//        return time;
//    }
//
//    public void setTime(int time) {
//        this.time = time;
//    }
//}

@RequiresApi(api = Build.VERSION_CODES.O)
public class CustomerPage extends AppCompatActivity implements GeoTask.Geo, GetDuration.Geo {
    private driverDataBase driverDB = new driverDataBase(this);

    RecyclerView recyclerView;
    driverAdapter driverAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference customerDatabaseReference;
    ArrayList<Driver> list;
    private static ArrayList<Driver> filtered;
    int capacity ;//TODO :get from user input
    int time;//TODO :get from user input.
    String starting;
    ArrayList<Set> temp;
    int driverToCustomer, customerToDestination;
    ArrayList<Integer> duration; //store the result after the calculation
    private EditText driverName, nameTxt,capacityTxt,timeTxt;
    private Button conDriver,conName;
    String customerName;
    ArrayList<String> all4Driver;
    ArrayList<String> all6Driver;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);
        firebaseDatabase = FirebaseDatabase.getInstance("https://e-hailing-um-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Driver");
        customerDatabaseReference = firebaseDatabase.getReference("Customer");
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        filtered = new ArrayList<>();
        temp=new ArrayList<>();
        duration=new ArrayList<>();
        driverAdapter = new driverAdapter(this,filtered);
        recyclerView.setAdapter(driverAdapter);
        capacity=4;
        time=20;
        starting= "University Malaya";
        recyclerView = findViewById(R.id.list);
        driverName=findViewById(R.id.driverName);
        nameTxt=findViewById(R.id.nameTxt);
        capacityTxt=findViewById(R.id.capacityTxt);
        timeTxt=findViewById(R.id.timeTxt);
        conDriver=findViewById(R.id.conDriver);
        conName=findViewById(R.id.conName);
         all4Driver=new ArrayList<>();

        conName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtered.clear();
                list.clear();
                temp.clear();
                customerName=nameTxt.getText().toString();
                capacity=Integer.parseInt(capacityTxt.getText().toString());
                time=Integer.parseInt(timeTxt.getText().toString());
                // TODO : recycler view
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        filtered.clear();
                        list.clear();
                        temp.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            Driver dr=ds.getValue(Driver.class);
                            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + dr.getCurrentLongLa() + "&destinations=" + starting + "&mode=driving&language=eg-EG&avoid=tolls&key=AIzaSyAg5EmD0YQUHjrMd5Aq0qwLaY24ZZCL3LI";
                            new GeoTask(CustomerPage.this).execute(url);
                            final Handler handler = new Handler(Looper.getMainLooper());
                            list.add(dr);
                            Log.d("hahaha","sdasldwthqehqthqehhqqtheqhh"+list.size());
                        }

                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for(int i=0;i<list.size();i++){
                                    try{
                                        if(list.get(i).getCapacity()>=capacity&&list.get(i).getStatus().equals("Available")){
                                            Log.d("sahigvaoivbiavb0w9ugr","sdasld"+temp.get(i));
                                            if(temp.get(i).getCheck()==true){
                                                filtered.add(list.get(i));
                                                filtered.get(filtered.size()-1).setArrivedTime(String.valueOf(temp.get(i).getTime())); //TODO : add with the current time
                                            }
                                        }
                                    }catch (IndexOutOfBoundsException e){
                                        Toast.makeText(CustomerPage.this, "Internet Connection weak, Please connect again", Toast.LENGTH_SHORT).show();

                                    }
                                }
                                driverAdapter.notifyDataSetChanged();
                                Log.d("hahaha","sdasld"+filtered.size());
                            }
                        }, 2000);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Log.d("hahaha","sdasld"+list.size());

            }
        });

    }

    @Override
    public void setDouble(String result) {
                String res[]=result.split(",");
                int min=(int)(Double.parseDouble(res[0])/60);
                if(Double.parseDouble(res[0])/60<=time){
                    temp.add(new Set(true,min));
                }else{
                   temp.add(new Set(false,min));
                }
                Log.d("hahaha","sdasld"+temp.toString());
            }

    @Override
    public void getDuration(String result) {
        String res[]=result.split(",");
        duration.add((int)(Double.parseDouble(res[0])/60));
        Log.d("hahaha","sdasld"+temp.toString());
    }


    //TODO: write all the method
    public void request(String userName, String driverName,int driverToCustomer,int customerToDestination) {
        Thread thread= new Thread(new Runnable() {
            @Override
            public void run() {
                setToWaiting(userName);
                setToUnavailable(driverName);
                //TODO: set the text view become "Your driver is on the way"
                final Handler handler1 = new Handler(Looper.getMainLooper());
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       setToPickedUp(userName);// TODO: set the text view

                    }
                }, driverToCustomer*1000);
                final Handler handler2 = new Handler(Looper.getMainLooper());
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setToReached(userName);// TODO: set the text view
                        setToAvailable(driverName);
                        setDriverLocation(driverName);


                    }
                }, (driverToCustomer+customerToDestination)*1000);
            }
        });
        thread.start();

    }

    public void setToPending(String userName){
        customerDatabaseReference.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               dataSnapshot.getRef().child("status").setValue("Pending");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CustomerPage.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setToWaiting(String userName){
        customerDatabaseReference.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("status").setValue("Waiting");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CustomerPage.this, "Please reenter the name ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setToPickedUp(String userName){
        customerDatabaseReference.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("status").setValue("Picked Up");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CustomerPage.this, "Please re-enter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setToReached(String userName){
        customerDatabaseReference.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("status").setValue("Reached");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CustomerPage.this, "Please re-enter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setToUnavailable(String driverName){
        databaseReference.child(driverName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("status").setValue("Unavailable");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CustomerPage.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setToAvailable(String driverName){
        databaseReference.child(driverName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("status").setValue("Available");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CustomerPage.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setRating(String driverName){
        databaseReference.child(driverName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               int number= dataSnapshot.child("numberOfRating").getValue(Integer.class);
               double rating=dataSnapshot.child("rating").getValue(Double.class);
               set(number,rating,driverName,Double.parseDouble(capacityTxt.getText()
               .toString()));// TODO :get the rating from user input
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CustomerPage.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void set(int numberOfRating,double rating, String driverName,double increasedRating){
        double result= (rating*numberOfRating+increasedRating)/(numberOfRating+1);
        databaseReference.child(driverName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("rating").setValue(round(result, 2));
                dataSnapshot.getRef().child("numberOfRating").setValue(numberOfRating+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CustomerPage.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void setDriverLocation(String driverName){
        databaseReference.child(driverName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String LongLa= "join the long la"; //TODO: put the user longla in here
                dataSnapshot.getRef().child("currentLongLa").setValue(LongLa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CustomerPage.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setCusAndArr(String driverName, String customerName,String arrivedTime){
        databaseReference.child(driverName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("customer").setValue(customerName);
                dataSnapshot.getRef().child("arrivedTime").setValue(arrivedTime);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CustomerPage.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAll4Location(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                all4Driver.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Driver dr= ds.getValue(Driver.class);
                    if(dr.getCapacity()==4){
                        all4Driver.add(dr.getCurrentLongLa());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CustomerPage.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getAll6Location(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                all6Driver.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Driver dr= ds.getValue(Driver.class);
                    if(dr.getCapacity()==6){
                        all6Driver.add(dr.getCurrentLongLa());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CustomerPage.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });

    }





}
/*
 String urlToCustomer = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + driverLongLa + "&destinations=" + CustomerLongLa + "&mode=driving&language=eg-EG&avoid=tolls&key=AIzaSyAg5EmD0YQUHjrMd5Aq0qwLaY24ZZCL3LI";
                    new GetDuration(CustomerPage.this).execute(urlToCustomer);
 String urlToDestination = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + CustomerLongLa + "&destinations=" + destinationLongLa + "&mode=driving&language=eg-EG&avoid=tolls&key=AIzaSyAg5EmD0YQUHjrMd5Aq0qwLaY24ZZCL3LI";
                    new GetDuration(CustomerPage.this).execute(urlToDestination);
 */
















