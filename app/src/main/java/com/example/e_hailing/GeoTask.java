package com.example.e_hailing;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/*The instance of this class is called by "MapsActivty",to get the time taken reach the destination from Google Distance Matrix API in background.
  This class contains interface "Geo" to call the function setDriver(String) defined in "MapsActivity.class" to add the time into the list.*/
public class GeoTask extends AsyncTask<String, Void, String> {
//    ProgressDialog pd;
    Context mContext;
    Double duration;
    Geo geo1;
    //constructor is used to get the context.
    public GeoTask(Context mContext) {
        this.mContext = mContext;
        geo1= (Geo) mContext;
    }

    //This function is executed before before "doInBackground(String...params)" is executed
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    //This function is executed after the execution of "doInBackground(String...params)" to  call "setDriver(Double)" defined in "MainActivity.java"
    @Override
    protected void onPostExecute(String aDouble) {
        super.onPostExecute(aDouble);
        if(aDouble!=null)
        {
            geo1.setDriver(aDouble);
        }
        else
            Toast.makeText(mContext, "Error4! Please Try Again", Toast.LENGTH_SHORT).show();
    }


    //This is  to parse the data  from the JSON file and return it as  a string , this override the doInBackground method
    @Override
    protected String doInBackground(String... params) {
        try {
            URL url=new URL(params[0]);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statuscode=con.getResponseCode();
            if(statuscode==HttpURLConnection.HTTP_OK)
            {
                BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb=new StringBuilder();
                String line=br.readLine();
                while(line!=null)
                {
                    sb.append(line);
                    line=br.readLine();
                }
                String json=sb.toString();
                Log.d("JSON",json);
                JSONObject root=new JSONObject(json);
                JSONArray array_rows=root.getJSONArray("rows");
                Log.d("JSON","array_rows:"+array_rows);
                JSONObject object_rows=array_rows.getJSONObject(0);
                Log.d("JSON","object_rows:"+object_rows);
                JSONArray array_elements=object_rows.getJSONArray("elements");
                Log.d("JSON","array_elements:"+array_elements);
                JSONObject  object_elements=array_elements.getJSONObject(0);
                Log.d("JSON","object_elements:"+object_elements);
                JSONObject object_duration=object_elements.getJSONObject("duration");
                JSONObject object_distance=object_elements.getJSONObject("distance");

                Log.d("JSON","object_duration:"+object_duration);
                return object_duration.getString("value")+","+object_distance.getString("value");

            }
        } catch (MalformedURLException e) {
            Log.d("error", "Mal formed URL");
        } catch (IOException e) {
            Log.d("error", "Exception occur");
        } catch (JSONException e) {
            Log.d("error","Api request failed");
        }

        return null;
    }

    //the interface that the class implement
    interface Geo{
        public void setDriver(String min);
    }

}
