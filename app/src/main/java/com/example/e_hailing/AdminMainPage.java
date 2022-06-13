package com.example.e_hailing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class AdminMainPage extends AppCompatActivity {
    private ImageButton customerPageBtn, driverPageBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);
        customerPageBtn=findViewById(R.id.customerPageBtn);
        driverPageBtn=findViewById(R.id.driverPageBtn);
        customerPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(AdminMainPage.this,adminCustomerPage2.class );
                startActivity(intent);
            }
        });
        driverPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminMainPage.this,adminDriverPage.class );
                startActivity(intent);
            }
        });
    }

}