package com.example.e_hailing;


import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetDirectionsData extends AsyncTask<Object, String, String> {

    String googleDirectionsData;
    GoogleMap mMap;
    String url;
    String distance, duration;
    LatLng latLng;
    public static int borrowDuration;

    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        latLng = (LatLng) objects[2];

        DownloadURL downloadURL = new DownloadURL();
        try {
            googleDirectionsData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s){


        HashMap<String, String> directionsList = null;
        DataParser parser = new DataParser();
        directionsList = parser.parseDirections(s);
        duration = directionsList.get("duration");
        distance = directionsList.get("distance");

        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.title("Duration = " + duration);
        markerOptions.snippet("Distance = " + distance);
        Log.d("fouhfufhoufhdosif",duration);
        String temp[]= duration.split(" ");
        if(temp.length==2){
            borrowDuration=Integer.parseInt(temp[0]);
        }else{
            borrowDuration=Integer.parseInt(temp[0])*60+Integer.parseInt(temp[2]);
        }

        mMap.addMarker(markerOptions);

        String[] directionsList1;
        DataParser parser1 = new DataParser();
        directionsList1 = parser1.parseDirections1(s);
        displayDirection(directionsList1);


    }


    public void displayDirection(String[] directionsList) {
        int count = directionsList.length;
        for (int i = 0; i<count; i++) {
            PolylineOptions options = new PolylineOptions();
            options.color(Color.BLUE);
            options.width(10);
            options.addAll(PolyUtil.decode(directionsList[i]));

            mMap.addPolyline(options);
        }

    }
}