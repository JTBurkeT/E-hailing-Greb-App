package com.example.e_hailing;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/*
This is the controller class of the admin main page which the admin will reach here after login

 */
public class AdminMainPage extends AppCompatActivity {
    private ImageButton customerPageBtn, driverPageBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);
        customerPageBtn=findViewById(R.id.customerPageBtn);
        driverPageBtn=findViewById(R.id.driverPageBtn);

        //This function navigate the user to the adminCustomer page
        customerPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminMainPage.this,adminCustomerPage2.class );
                startActivity(intent);
            }
        });

        //This function navigate the user to the adminDriverPage
        driverPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AdminMainPage.this,adminDriverPage.class );
                startActivity(intent);
            }
        });


    }


    //This is to override the onBackPressed method and set it to appear the logout dialog after press the reversed button
    @Override
    public void onBackPressed(){

                Dialog logoutDialog = new Dialog(AdminMainPage.this);
                logoutDialog.setContentView(R.layout.logout_dialog);
                Button yesBtn= logoutDialog.findViewById(R.id.yesBtn);
                Button noBtn= logoutDialog.findViewById(R.id.noBtn);


                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AdminMainPage.this,AdminLoginPage.class);
                        startActivity(intent);
                        finish();
                    }
                });

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logoutDialog.dismiss();
                    }
                });
        logoutDialog.show();
    }

}