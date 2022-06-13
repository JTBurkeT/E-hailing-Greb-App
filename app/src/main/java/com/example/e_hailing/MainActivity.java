package com.example.e_hailing;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private TextView registerTxt, adminLoginTxt;
    private EditText userNameTxt, passwordTxt;
    private Button loginBtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public static String userName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        firebaseDatabase = FirebaseDatabase.getInstance("https://e-hailing-um-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Customer");



        registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,RegisterPage.class);
                startActivity(intent);
            }
        });

        adminLoginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this,AdminLoginPage.class);
                startActivity(intent);
            }
        });


        //Todo: the place changes
        loginBtn.setOnClickListener(new View.OnClickListener() {
            String password="";
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (userNameTxt.getText().toString().equals("") || passwordTxt.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Please make sure all the field is filled", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child(userNameTxt.getText().toString()).child("password").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           password =dataSnapshot.getValue(String.class);
                            Log.d(TAG, "Value is: " + password);
                            try{
                                if (password.equals(passwordTxt.getText().toString())) {
                                    userName=userNameTxt.getText().toString();
                                    passwordTxt.setText("");
                                    userNameTxt.setText("");
                                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            }catch (NullPointerException e){
                                Toast.makeText(MainActivity.this, "UserName not found ", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(MainActivity.this, "UserName not found ", Toast.LENGTH_SHORT).show();


                        }
                    });


                }
            }
        });


    }

    public void init(){
        registerTxt=findViewById(R.id.registerTxt);
        adminLoginTxt=findViewById(R.id.adminLoginTxt);
        userNameTxt=findViewById(R.id.userNameTxt);
        passwordTxt=findViewById(R.id.passwordTxt);
        loginBtn=findViewById(R.id.loginBtn);
    }

}