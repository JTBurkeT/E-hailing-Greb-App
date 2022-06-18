package com.example.e_hailing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.util.NumberUtils;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
This class set is created to store the value of time and the boolean whether the driver is suitable or not
 */
class Set{
    //store the boolean and time when receiving the result from the requested API
    private Boolean check;
    private int time;

    //The constructor
    public Set(Boolean check, int time){
        this.time=time;
        this.check=check;
    }

    //Getter and setter method
    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}

/*
This is the page where user can make the request and search for driver, comment, search the destination and etc
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMarkerClickListener,
        GeoTask.Geo,
        GetDuration.Geo {


    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentLocationmMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 10000;
    double latitude, longitude;
    double end_latitude, end_longitude;
    EditText etSource;
    Button btshow, btnComment, logoutBtn;
    Dialog myDialog, commentDialog, logoutDialog;
    List<Address> addressList;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference customerDatabaseReference;
    ArrayList<Driver> list;
    private static ArrayList<Driver> filtered;
    String nowTime;
    String endingLaLong;
    ArrayList<Set> temp;
    String time;
    int capacity;
    int driverToCustomer, customerToDestination;
    ArrayList<Integer> duration; //store the result after the calculation
    String customerName, driverName;
    ArrayList<String> all4Driver, all6Driver;
    String longLa;
    EditText tf_location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        all4Driver = new ArrayList<>();
        all6Driver = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance("https://e-hailing-um-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Driver");
        customerDatabaseReference = firebaseDatabase.getReference("Customer");
        getAll4Location();
        getAll6Location();
        customerName = MainActivity.userName;
        list = new ArrayList<>();
        temp = new ArrayList<>();
        filtered = new ArrayList<>();
        duration = new ArrayList<>();
        tf_location = findViewById(R.id.TF_location);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();

        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Thread myThread = null;

        Runnable runnable = new CountDownRunner();
        myThread = new Thread(runnable);
        myThread.start();

        EditText tf_location = findViewById(R.id.TF_location);
        String location = tf_location.getText().toString();
        Places.initialize(getApplicationContext(), "AIzaSyAg5EmD0YQUHjrMd5Aq0qwLaY24ZZCL3LI");
        tf_location.setFocusable(false);
        tf_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields
                ).build(MapsActivity.this);
                startActivityForResult(intent, 100);
            }
        });


    }


    //This is the method to display the app time for the user
    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    TextView txtCurrentTime = (TextView) findViewById(R.id.timerText);
                    Date dt = new Date();

                    int minutes = dt.getMinutes() % 24;
                    int seconds = dt.getSeconds();
                    String minute = String.format("%02d", minutes);
                    String second = String.format("%02d", seconds);
                    String curTime = minute + " : " + second;
                    txtCurrentTime.setText(curTime);
                    nowTime = minute + ":" + second;
                } catch (Exception e) {
                }
            }
        });
    }


    //This inner class is to create the multi thread which sleep one second and call the function again to set the time
    class CountDownRunner implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }


    //This is to get the permission to read the user location
    @SuppressLint({"MissingPermission", "MissingSuperCall"})
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (client == null) {
                            bulidGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            bulidGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
    }



    //This is to build google API client
    protected synchronized void bulidGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();

    }


    //This iis to read the current location of the user and display it as the marker
    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastlocation = location;
        if (currentLocationmMarker != null) {
            currentLocationmMarker.remove();

        }
        Log.d("lat = ", "" + latitude);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentLocationmMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(6));

        if (client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
        }
    }


    //This is the method which defined the function button
    @SuppressLint("MissingPermission")
    public void onClick(View v) {
        Object dataTransfer[] = new Object[2];
        GetDirectionsData getDirectionsData = new GetDirectionsData();

        switch (v.getId()) {
            //The search button is to search the destination of the customer and display the route within the origin and destination, and also get the duration form the origin to the deastination

            case R.id.B_search:
                tf_location = findViewById(R.id.TF_location);
                String location = tf_location.getText().toString();


                Places.initialize(getApplicationContext(), "AIzaSyAg5EmD0YQUHjrMd5Aq0qwLaY24ZZCL3LI");
                tf_location.setFocusable(false);
                tf_location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        List<Place.Field> fields = Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG);
                        Intent intent = new Autocomplete.IntentBuilder(
                                AutocompleteActivityMode.OVERLAY, fields
                        ).build(MapsActivity.this);
                        startActivityForResult(intent, 100);
                    }
                });


                if (!location.equals("")) {
                    Geocoder geocoder = new Geocoder(this);
                    mMap.clear();
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        bulidGoogleApiClient();
                        mMap.setMyLocationEnabled(true);
                    }
                    try {
                        addressList = geocoder.getFromLocationName(location, 5);

                        if (addressList != null) {
                            LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                            dataTransfer = new Object[3];
                            String url = getDirectionsUrl3();
                            dataTransfer[0] = mMap;
                            dataTransfer[1] = url;
                            dataTransfer[2] = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(6));
                            endingLaLong = String.valueOf(addressList.get(0).getLatitude()) + "," + String.valueOf(addressList.get(0).getLongitude());

                            try{
                                getDirectionsData.execute(dataTransfer);
                            }catch(NullPointerException e){
                                Toast.makeText(this, "Please Enter the location within 300 km", Toast.LENGTH_SHORT).show();
                            }

                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    customerToDestination = getDirectionsData.borrowDuration;
                                }
                            }, 1000);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                break;

                //This is to set the function of the button which will show all the 4-pax driver
            case R.id.fourDriver:

                getAll4Location();
                mMap.clear();
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    bulidGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }

                //Here is to make a delay so that all the data already fetched from the database
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (String x : all4Driver) {
                            String temp[] = x.split(",");
                            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(temp[0]), Double.parseDouble(temp[1]))).title("4-pax car position").icon(BitmapDescriptorFactory.fromResource(R.drawable.fourpaxcar)));

                        }

                        Toast.makeText(MapsActivity.this, "Showing 4-pax car", Toast.LENGTH_SHORT).show();
                    }
                }, 600);

                mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
                break;

            //This is to set the function of the button which will show all the 6-pax driver
            case R.id.sixDriver:

                getAll6Location();
                mMap.clear();
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    bulidGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }

                //Here is to make a delay so that all the data already fetched from the database
                final Handler handler2 = new Handler(Looper.getMainLooper());
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (String x : all6Driver) {
                            String temp[] = x.split(",");
                            mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(temp[0]), Double.parseDouble(temp[1]))).title("6-pax car position").icon(BitmapDescriptorFactory.fromResource(R.drawable.sixpaxcar)));

                        }

                        Toast.makeText(MapsActivity.this, "Showing 6-pax car", Toast.LENGTH_SHORT).show();
                    }
                }, 600);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
                break;


                //this is to set the function to open the request bottom dialog
            case R.id.searchDriver:


                btnComment = findViewById(R.id.btnComment);

                        if (tf_location.getText().toString().equals("")) {
                            Toast.makeText(MapsActivity.this, "Please enter the Destination first", Toast.LENGTH_SHORT).show();
                        } else {


                            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MapsActivity.this);
                            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
                            bottomSheetDialog.setCanceledOnTouchOutside(false);

                            TextView etUsername = bottomSheetDialog.findViewById(R.id.et_username);
                            EditText et_arrivaltime = bottomSheetDialog.findViewById(R.id.et_arrivaltime);
                            EditText et_capacity = bottomSheetDialog.findViewById(R.id.et_capacity);
                            Button btUpdateconfirm = bottomSheetDialog.findViewById(R.id.bt_updateconfirm);
                            etUsername.setText(etUsername.getText().toString() + customerName);


                            myDialog = new Dialog(MapsActivity.this);



                            //This code is to make the searching request and display the suitable driver
                            btUpdateconfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    driverAdapter driverAdapter;
                                    myDialog.setContentView(R.layout.custompopup);
                                    setToPending(customerName);
                                    TextView txtClose = myDialog.findViewById(R.id.txtclose);
                                    TextView priceTxt = myDialog.findViewById(R.id.priceTxt);

                                    Button conDriver = (Button) myDialog.findViewById(R.id.btn_confirmDriver);

                                    RecyclerView recyclerView = myDialog.findViewById(R.id.driverList);
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(MapsActivity.this));
                                    driverAdapter = new driverAdapter(MapsActivity.this, filtered);
                                    recyclerView.setAdapter(driverAdapter);
                                    EditText driverNameTxt = myDialog.findViewById(R.id.driverNameTxt);
                                    Button btn_confirmDriver = myDialog.findViewById(R.id.btn_confirmDriver);
                                    ProgressDialog pd = new ProgressDialog(MapsActivity.this);
                                    pd.setMessage("Searching Suitable Driver");
                                    pd.setCancelable(false);
                                    priceTxt.setText("Price: " + round(getPrice(capacity, customerToDestination), 2));

                                    txtClose.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            myDialog.dismiss();
                                        }
                                    });


                                    try {
                                        if (et_arrivaltime.getText().toString().equals("") || et_capacity.getText().toString().equals("")) {
                                            Toast.makeText(MapsActivity.this, "Please re-enter the field provided ", Toast.LENGTH_SHORT).show();
                                        }else if(!et_arrivaltime.getText().toString().equals("") && !et_capacity.getText().toString().equals("")){
                                            if(matchTimeFormat(et_arrivaltime.getText().toString())&& matchIntegerFormat(et_capacity.getText().toString())  ){
                                                time = et_arrivaltime.getText().toString();
                                                capacity = Integer.parseInt(et_capacity.getText().toString());
                                                Thread thread = new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        databaseReference.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                filtered.clear();
                                                                list.clear();
                                                                temp.clear();
                                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                                    Driver dr = ds.getValue(Driver.class);
                                                                    String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + dr.getCurrentLongLa() + "&destinations=" + latitude + "," + longitude + "&mode=driving&language=eg-EG&avoid=tolls&key=AIzaSyAg5EmD0YQUHjrMd5Aq0qwLaY24ZZCL3LI";
                                                                    new GeoTask(MapsActivity.this).execute(url);
                                                                    final Handler handler = new Handler(Looper.getMainLooper());
                                                                    list.add(dr);

                                                                }

                                                                final Handler handler = new Handler(Looper.getMainLooper());
                                                                handler.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        pd.dismiss();
                                                                        for (int i = 0; i < list.size(); i++) {
                                                                            try {
                                                                                String[] st = nowTime.split(":");
                                                                                if (list.get(i).getCapacity() >= capacity && list.get(i).getStatus().equals("Available")) {
                                                                                    Log.d("sahigvaoivbiavb0w9ugr", "sdasld" + temp.get(i).getCheck());
                                                                                    if (temp.get(i).getCheck() == true) {
                                                                                        filtered.add(list.get(i));
                                                                                        filtered.get(filtered.size() - 1).setArrivedTime(timeFormatter(st[0] + ":" + String.valueOf(temp.get(i).getTime() + Integer.parseInt(st[1])))); //TODO : add with the current time
                                                                                    }
                                                                                }
                                                                            } catch (IndexOutOfBoundsException e) {
                                                                                Toast.makeText(MapsActivity.this, "Internet Connection weak, Please connect again", Toast.LENGTH_SHORT).show();

                                                                            }
                                                                        }
                                                                        driverAdapter.notifyDataSetChanged();
                                                                    }
                                                                }, 2500);
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });

                                                    }
                                                });
                                                thread.start();
                                                myDialog.show();
                                                pd.show();

                                            }else{
                                                Toast.makeText(MapsActivity.this, "Please enter according to the format", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    } catch (NumberFormatException e) {
                                        Toast.makeText(MapsActivity.this, "Please enter in valid form ", Toast.LENGTH_SHORT).show();
                                    } catch (ArrayIndexOutOfBoundsException a) {
                                        Toast.makeText(MapsActivity.this, "Please enter time in valid format", Toast.LENGTH_SHORT).show();
                                    }


                                    //This is to confirm the driver and make the request
                                    btn_confirmDriver.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            driverName = driverNameTxt.getText().toString();
                                            databaseReference.child(driverName).child("currentLongLa").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    longLa = dataSnapshot.getValue(String.class);
                                                    try {
                                                        if (longLa.equals(null)) {
                                                            Toast.makeText(MapsActivity.this, "Driver " + driverName + " not found, Please Try Again", Toast.LENGTH_SHORT).show();

                                                        } else {
                                                            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + longLa + "&destinations=" + latitude + "," + longitude + "&mode=driving&language=eg-EG&avoid=tolls&key=AIzaSyAg5EmD0YQUHjrMd5Aq0qwLaY24ZZCL3LI";
                                                            new GetDuration(MapsActivity.this).execute(url);
                                                            myDialog.dismiss();
                                                            bottomSheetDialog.dismiss();


                                                            final Handler handler = new Handler(Looper.getMainLooper());
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    request(customerName, driverName, driverToCustomer, customerToDestination);
                                                                }
                                                            }, 500);
                                                            Toast.makeText(MapsActivity.this, " Request Successfully made ", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (NullPointerException e) {
                                                        Toast.makeText(MapsActivity.this, "Driver " + driverName + " not found, Please Try Again", Toast.LENGTH_SHORT).show();
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });


                                        }
                                    });



                                    TextView TxtClose = myDialog.findViewById(R.id.txtclose);
                                    TxtClose.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            myDialog.dismiss();
                                        }
                                    });


                                }
                            });
                            bottomSheetDialog.show();
                        }


                break;

                //This is to set the function to show the rating dialog
            case R.id.btnComment:


                        commentDialog = new Dialog(MapsActivity.this);
                        commentDialog.setContentView(R.layout.comment_dialog);
                        TextView driverNameView = commentDialog.findViewById(R.id.driverNameView);
                        EditText rating = commentDialog.findViewById(R.id.ratingTxt);
                        Button submit = commentDialog.findViewById(R.id.btnSubmit);
                        driverNameView.setText(driverNameView.getText().toString() + driverName);
                        TextView closeTxt = commentDialog.findViewById(R.id.btnCloseDialog);
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!rating.getText().toString().equals("")&&matchRatingFormat(rating.getText().toString())) {
                                    setRating(driverName, Double.parseDouble(rating.getText().toString()));
                                    Toast.makeText(MapsActivity.this, "Rate driver " + driverName + " successfully. Thank You", Toast.LENGTH_SHORT).show();
                                    commentDialog.dismiss();
                                    btnComment.setVisibility(View.INVISIBLE);
                                }else{
                                    Toast.makeText(MapsActivity.this, "Please enter a valid rating", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                        closeTxt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                commentDialog.dismiss();
                            }
                        });
                        commentDialog.show();



                break;

            case R.id.logoutBtn:


                        logoutDialog = new Dialog(MapsActivity.this);
                        logoutDialog.setContentView(R.layout.logout_dialog);
                        Button yesBtn= logoutDialog.findViewById(R.id.yesBtn);
                        Button noBtn= logoutDialog.findViewById(R.id.noBtn);
                        logoutDialog.show();

                        yesBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setToFree(customerName);
                                finish();
                                onBackPressed();
                            }
                        });

                        noBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                logoutDialog.dismiss();
                            }
                        });


                break;



        }
    }



    //This method is override the serDriver method od the GeoTask class to get the suitable driver with the list
    @Override
    public void setDriver(String result) {
        String res[] = result.split(",");
        int min = (int) (Double.parseDouble(res[0]) / 60);
        int totalTime = min + customerToDestination;
        Log.d("hahaha", "total time" + totalTime);
        int timeDif = timeDif(nowTime, String.valueOf(time));
        if (totalTime <= timeDif) {
            temp.add(new Set(true, totalTime));
        } else {
            temp.add(new Set(false, totalTime));
        }
        Log.d("hahaha", "sdasld" + time);
    }

    //This method is override the getDuration method od the getDuration class to get the duration from driver to customer
    @Override
    public void getDuration(String result) {
        String res[] = result.split(",");
        driverToCustomer = (int) (Double.parseDouble(res[0]) / 60);
        Log.d("hahaha", "driver to customer" + driverToCustomer);
    }



    private String getDirectionsUrl2() {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin=" + longLa);
        googleDirectionsUrl.append(("&destination=" + latitude + "," + longitude));
        googleDirectionsUrl.append("&key=" + "AIzaSyAg5EmD0YQUHjrMd5Aq0qwLaY24ZZCL3LI");

        return googleDirectionsUrl.toString();
    }

    private String getDirectionsUrl3() {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin=" + latitude + "," + longitude);
        googleDirectionsUrl.append(("&destination=" + addressList.get(0).getLatitude() + "," + addressList.get(0).getLongitude()));
        googleDirectionsUrl.append("&key=" + "AIzaSyAg5EmD0YQUHjrMd5Aq0qwLaY24ZZCL3LI");

        return googleDirectionsUrl.toString();
    }



    //This method is to get tje location request if the user
    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }


    //This is to check whether the user give the permission to access his location
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_CODE);
            }
            return false;

        } else
            return true;
    }


    //This all is to override the method in the GoogleMap
    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    //Set the market to draggable
    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        marker.setDraggable(true);
        return false;
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    //Read the latitude and longitude of the marker
    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        end_latitude = marker.getPosition().latitude;
        end_longitude = marker.getPosition().longitude;
        endingLaLong = String.valueOf(end_longitude) + "," + String.valueOf(end_latitude);
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }

    //This method is to get the location of the destination
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        etSource = findViewById(R.id.TF_location);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            etSource.setText(place.getAddress());

        } else if (requestCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(getApplicationContext(), status.getStatusMessage()
                    , Toast.LENGTH_SHORT).show();
        }
    }

    //All the method

    //This method is to get all the 4-pax driver
    public void getAll4Location() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                all4Driver.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Driver dr = ds.getValue(Driver.class);
                    if (dr.getCapacity() == 4) {
                        all4Driver.add(dr.getCurrentLongLa());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //This method is to get all the 6-pax driver
    public void getAll6Location() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                all6Driver.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Driver dr = ds.getValue(Driver.class);
                    if (dr.getCapacity() == 6) {
                        all6Driver.add(dr.getCurrentLongLa());
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //This method is to rate the driver
    public void setRating(String driverName, Double rate) {
        databaseReference.child(driverName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int number = dataSnapshot.child("numberOfRating").getValue(Integer.class);
                double rating = dataSnapshot.child("rating").getValue(Double.class);
                set(number, rating, driverName, rate);// TODO :get the rating from user input
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //this method is to set the rating in the database after the user rate
    public void set(int numberOfRating, double rating, String driverName, double increasedRating) {
        double result = (rating * numberOfRating + increasedRating) / (numberOfRating + 1);
        databaseReference.child(driverName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("rating").setValue(round(result, 2));
                dataSnapshot.getRef().child("numberOfRating").setValue(numberOfRating + 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //This method is to round the value into desired decimal places
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    //This method is to make the request for the driver
    public void request(String userName, String driverName, int driverToCustomer, int customerToDestination) {
        GetDirectionsData getDirectionsData = new GetDirectionsData();
        mMap.clear();

        TextView statusTxt= findViewById(R.id.statusTxt);
        statusTxt.setText("Your driver is on his way to pick you ");
        statusTxt.setVisibility(View.VISIBLE);
        String[] st = nowTime.split(":");
        setCusAndArr(driverName,userName,timeFormatter(st[0] + ":" + String.valueOf(driverToCustomer+customerToDestination + Integer.parseInt(st[1]))));
        setToWaiting(userName);
        setCapacity(userName,capacity);
        setDestinationLongLa(userName,endingLaLong);
        setStartingLongLa(userName,latitude + "," + longitude);
        cusArrivedTime(userName,timeFormatter(st[0] + ":" + String.valueOf(driverToCustomer+customerToDestination + Integer.parseInt(st[1]))));


        Object dataTransfer[] = new Object[3];
        String url = getDirectionsUrl2();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        dataTransfer[2] = new LatLng(latitude, longitude);

        String temp[]=longLa.split(",");
        LatLng driverLocation=new LatLng(Double.parseDouble(temp[0]),Double.parseDouble(temp[1]));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(driverLocation);
        markerOptions.title("Driver Location");

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.sixpaxcar));

        getDirectionsData.execute(dataTransfer);

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMap.addMarker(markerOptions);
            }
        }, 300);



        final Handler handler1 = new Handler(Looper.getMainLooper());
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                GetDirectionsData getDirectionsData2 = new GetDirectionsData();
                setToPickedUp(userName);
                mMap.clear();
                LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                Object dataTransfer2 []= new Object[3];
                String url = getDirectionsUrl3();
                dataTransfer2[0] = mMap;
                dataTransfer2[1] = url;
                dataTransfer2[2] = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

                statusTxt.setText("On the way to the destination ");

                LatLng driverLocation=new LatLng(latitude,longitude);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(driverLocation);
                markerOptions.title("Driver Location");
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.sixpaxcar));
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMap.addMarker(markerOptions);
                    }
                }, 300);
                getDirectionsData2.execute(dataTransfer2);

            }
        }, driverToCustomer * 1000);

        final Handler handler2 = new Handler(Looper.getMainLooper());
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                statusTxt.setText("Reached, have a good day.");
                setToReached(userName);
                reSet(driverName);
                setToFree(customerName);
                btnComment.setVisibility(View.VISIBLE);
            }
        }, (driverToCustomer + customerToDestination) * 1000);

        final Handler handler3 = new Handler(Looper.getMainLooper());
        handler3.postDelayed(new Runnable() {
            @Override
            public void run() {
                statusTxt.setVisibility(View.INVISIBLE);
                mMap.clear();

            }
        }, (driverToCustomer + customerToDestination+2) * 1000);
}


//This method is to set the customer arrival time
public void cusArrivedTime(String customerName,String arrivedTime){
    customerDatabaseReference.child(customerName).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            dataSnapshot.getRef().child("expectedArrivalTime").setValue(arrivedTime);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(MapsActivity.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
        }
    });
}


//this method is to set the capacity of the customer
    public void setCapacity(String customerName,int capacity){
        customerDatabaseReference.child(customerName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("capacity").setValue(capacity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //This method is to set the destination location for the customer
    public void setDestinationLongLa(String customerName,String destination){
        customerDatabaseReference.child(customerName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("destinationLongLa").setValue(formatLongLa(destination));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //This method is to set the starting location of the customer
    public void setStartingLongLa(String customerName,String starting){
        customerDatabaseReference.child(customerName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("startingLongLa").setValue(formatLongLa(starting));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }

//this method is to set the status of customer to pending
    public void setToPending(String userName){
        customerDatabaseReference.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("status").setValue("Pending");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //this method is to set the status of customer to waiting
    public void setToWaiting(String userName){
        customerDatabaseReference.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("status").setValue("Waiting");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Please reenter the name ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //this method is to set the status of customer to picked up
    public void setToPickedUp(String userName){
        customerDatabaseReference.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("status").setValue("Picked Up");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Please re-enter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //this method is to set the status of customer to reached
    public void setToReached(String userName){
        customerDatabaseReference.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("status").setValue("Reached");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Please re-enter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //this method is to set the status of customer to free which mean to action his do
    public void setToFree(String userName){
        customerDatabaseReference.child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("status").setValue("");
                dataSnapshot.getRef().child("expectedArrivalTime").setValue("");
                dataSnapshot.getRef().child("destinationLongLa").setValue("");
                dataSnapshot.getRef().child("startingLongLa").setValue("");
                dataSnapshot.getRef().child("capacity").setValue(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }



    //this method is to set the arrived time and the status , and the customer of the driver
    public void setCusAndArr(String driverName, String customerName,String arrivedTime){
        databaseReference.child(driverName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("customer").setValue(customerName);
                dataSnapshot.getRef().child("arrivedTime").setValue(arrivedTime);
                dataSnapshot.getRef().child("status").setValue("Unavailable");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //This method  is to reset the driver after the journey have ended
    public void reSet(String driverName){
        databaseReference.child(driverName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String LongLa= endingLaLong; //TODO: put the user longla in here
                dataSnapshot.getRef().child("currentLongLa").setValue(formatLongLa(LongLa));
                dataSnapshot.getRef().child("customer").setValue("");
                dataSnapshot.getRef().child("arrivedTime").setValue("");
                dataSnapshot.getRef().child("status").setValue("Available");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MapsActivity.this, "Please reenter the name  ", Toast.LENGTH_SHORT).show();
            }
        });
    }


//This method is to calculate the time different between the user input and current time
    public  int  timeDif(String  start,String end){
        String[] split1 = start.split(":");
        String[] split2 = end.split(":");
        int []time1=new int[2];
        int[] time2=new int[2];
            time1[0]+=Integer.parseInt(split1[0]);
            time1[1]+=Integer.parseInt(split1[1]);
            time2[0]+=Integer.parseInt(split2[0]);
            time2[1]+=Integer.parseInt(split2[1]);

        if(time2[0]<time1[0]){
            time2[0]+=24;
        }
        int[] temp=new int[2];
        if(Integer.parseInt(split2[1])>=Integer.parseInt(split1[1])){
            temp[1]=Integer.parseInt(split2[1])-Integer.parseInt(split1[1]);
        }else {
            time2[0]-=1;
            temp[1]=60+Integer.parseInt(split2[1])-Integer.parseInt(split1[1]);
        }
        temp[0]=time2[0]-time1[0];
        return temp[0]*60 +temp[1];
    }


    //this method is to format the time format
    public static String timeFormatter(String time) {
        String[] split1 = time.split(":");
        int[] time1 = new int[2];
        time1[0] += Integer.parseInt(split1[0]);
        time1[1] += Integer.parseInt(split1[1]);
        while (time1[0] >= 24 ) {
            time1[0] -= 24;
        }
        while (time1[1] >= 60) {
            time1[0] += 1;
            time1[1]-=60;
        }

        if (time1[0] >= 24 ) {
            time1[0] -= 24;
        }

        if(String.valueOf(time1[0]).length()==1 && String.valueOf(time1[1]).length()==1) {
            return "0" + time1[0] + ":0" + time1[1];
        }
        else if(String.valueOf(time1[0]).length()==1) {
            return "0" + time1[0] + ":" + time1[1];
        }
        else if(String.valueOf(time1[1]).length()==1) {
            return  + time1[0] + ":0" + time1[1];
        }
        return time1[0]+":"+time1[1];
    }

    //this is to format the longitude and latitude to 6 decimal place
    public String formatLongLa(String LongLa){
        String temp[]=LongLa.split(",");
        double scale = Math.pow(10, 6);
        double str1=Math.round(Double.parseDouble(temp[0])*scale)/scale;
        double str2=Math.round(Double.parseDouble(temp[1])*scale)/scale;
        return str1+","+str2;
    }


    //This  method is to calculate the price of whole journey
    public double getPrice(int capacity, int totalTime){
        if(capacity<=4){
            return totalTime*4*0.30;
        }else{
            return totalTime*6*0.30;
        }
    }


    //This method is to check whether the user input time match the format
    public boolean matchTimeFormat(String str){
        Pattern p= Pattern.compile("[0-2][0-9]:[0-5][0-9]");
        Matcher m= p.matcher(str);
        return m.matches();
    }

    //This method is to check whether the user capacity match the format
    public boolean matchIntegerFormat(String str){
        Pattern p= Pattern.compile("[1-6]");
        Matcher m= p.matcher(str);
        return m.matches();
    }

    //This method is to check whether the user rating match the format
    public boolean matchRatingFormat(String str){
        Pattern p= Pattern.compile("[0-5].[0-9]");
        Matcher m= p.matcher(str);
        return m.matches();
    }



}