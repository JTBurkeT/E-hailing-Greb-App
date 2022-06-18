package com.example.e_hailing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
/*
    This is the controller of the splash activity which is the first page appear when open the app
 */
public class SplashActivity extends AppCompatActivity {
    Handler handler;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar= findViewById(R.id.progressBar);
        //this is to make the page continue for 3 second
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },3000);
        setProgressValue(5);
    }


    //this method is to set the progress bar value
    private void setProgressValue(int progress){
        progressBar.setProgress(progress);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                setProgressValue(progress*2);
            }
        });thread.start();
    }
}