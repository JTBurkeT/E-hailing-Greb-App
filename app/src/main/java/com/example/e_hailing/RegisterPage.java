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

public class RegisterPage extends AppCompatActivity {
    EditText UserName, Password ,Email ,ConfirmPassword;
    Button backBtn, registerBtn;
    customerDataBase DB; adminDataBase DB2;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        initView();
        DB = new customerDataBase(this);
        DB2 =new adminDataBase(this);
        DB2.insertAdmin();
        firebaseDatabase = FirebaseDatabase.getInstance("https://e-hailing-um-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Customer");

//        registerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(Password.getText().toString().equals(ConfirmPassword.getText().toString())){
//                    Boolean checkinsertdata = DB.registerCustomer(UserName.getText().toString(),Email.getText().toString(),Password.getText().toString());
//                    if(checkinsertdata==true)
//                        Toast.makeText(RegisterPage.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
//                    else
//                        Toast.makeText(RegisterPage.this, "Register Unsuccessful", Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(RegisterPage.this, "Please Make Sure the Password Inserted is Correct", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//        });

        //Todo: the place changed
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Password.getText().toString().equals("")|| !UserName.getText().toString().equals("")||!Email.getText().toString().equals("")){
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
                    Toast.makeText(RegisterPage.this, "Please make sure all the field is filled ", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    public  void initView(){
        UserName= findViewById(R.id.userNameText);
        Password= findViewById(R.id.passwordText);
        Email= findViewById(R.id.emailText);
        ConfirmPassword= findViewById(R.id.comfirmPassword);

        backBtn= findViewById(R.id.backBtn1);
        registerBtn=findViewById(R.id.registerBtn);
    }


}