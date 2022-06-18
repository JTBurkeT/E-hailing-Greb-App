package com.example.e_hailing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.xml.namespace.NamespaceContext;
/*
This class is the controller of the register page which user can create account in this page
 */
public class RegisterPage extends AppCompatActivity {
    EditText UserName, Password ,Email ,ConfirmPassword;
    Button backBtn, registerBtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        initView();
        firebaseDatabase = FirebaseDatabase.getInstance("https://e-hailing-um-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Customer");



        //set the button function as the conformation to create account
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Password.getText().toString().equals("")&& !UserName.getText().toString().equals("")&&!Email.getText().toString().equals("")&&!ConfirmPassword.getText().toString().equals("")){
                    if(Password.getText().toString().length()>6){
                        if(Password.getText().toString().equals(ConfirmPassword.getText().toString())){
                            CustomerObject c= new CustomerObject(UserName.getText().toString(),Email.getText().toString(),Password.getText().toString());
                            databaseReference.child(UserName.getText().toString()).setValue(c);
                            Toast.makeText(RegisterPage.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent =new Intent(RegisterPage.this,MainActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(RegisterPage.this, "Please Make Sure the Password Inserted is Correct", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterPage.this, "The Password must be longer than 6 character", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RegisterPage.this, "Please make sure all the field is filled ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //set the function to back to previous page
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                onBackPressed();
            }
        });

    }

    //initialize the widget in the page
    public  void initView(){
        UserName= findViewById(R.id.userNameTxt1);
        Password= findViewById(R.id.passwordText);
        Email= findViewById(R.id.emailText);
        ConfirmPassword= findViewById(R.id.comfirmPassword);

        backBtn= findViewById(R.id.backBtn1);
        registerBtn=findViewById(R.id.registerBtn);
    }


}