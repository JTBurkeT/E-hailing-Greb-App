package com.example.e_hailing;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
/*
This is controller class of teh admin Login page
 */
public class AdminLoginPage extends AppCompatActivity {
    private EditText adminIdTxt, passwordTxt;
    private Button loginBtn, backBtn;
    private adminDataBase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_page);
        init();
        DB= new adminDataBase(this);



        //This is to set the loginBtn perform the function get the password from the database and compare with the user input
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adminIdTxt.getText().toString().equals("") || passwordTxt.getText().toString().equals("")){
                    Toast.makeText(AdminLoginPage.this, "Please make sure all the field is filled", Toast.LENGTH_SHORT).show();
                } else {
                    String password = DB.getPassword(adminIdTxt.getText().toString());
                    if (password.equals(passwordTxt.getText().toString())) {
                        passwordTxt.setText("");
                        adminIdTxt.setText("");
                        Intent intent = new Intent(AdminLoginPage.this, AdminMainPage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AdminLoginPage.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //This is to set the function of the back button
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                onBackPressed();
            }
        });

    }

    //This method is to initialize all the widget in the page
    public void init() {
        adminIdTxt = findViewById(R.id.adminIdTxt);
        passwordTxt = findViewById(R.id.adminPasswordTxt);
        loginBtn = findViewById(R.id.LoginBtn);
        backBtn = findViewById(R.id.backBtn);


    }
}