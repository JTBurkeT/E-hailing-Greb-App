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

public class AdminLoginPage extends AppCompatActivity {
    private EditText adminIdTxt, passwordTxt;
    private Button loginBtn, backBtn;
    private TextView textView;private adminDataBase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_page);
        init();
        DB= new adminDataBase(this);


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
                    } else {
                        Toast.makeText(AdminLoginPage.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void init() {
        adminIdTxt = findViewById(R.id.adminIdTxt);
        passwordTxt = findViewById(R.id.adminPasswordTxt);
        loginBtn = findViewById(R.id.LoginBtn);
        backBtn = findViewById(R.id.backBtn);


    }
}