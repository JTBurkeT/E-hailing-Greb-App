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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.NamespaceContext;
/*
This class is the controller of the register page which user can create account in this page
 */
public class RegisterPage extends AppCompatActivity {
    EditText UserName, Password ,ConfirmPassword;
    Button backBtn, registerBtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    public EditText Email;


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
                    if(Password.getText().toString().length()>6&&matchEmailFormat(Email.getText().toString())){
                        if(Password.getText().toString().equals(ConfirmPassword.getText().toString())){
                            CustomerObject c= new CustomerObject(UserName.getText().toString(),Email.getText().toString(),Password.getText().toString());
                            databaseReference.child(UserName.getText().toString()).setValue(c);
                            sendMail(UserName.getText().toString());
                            Toast.makeText(RegisterPage.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent =new Intent(RegisterPage.this,MainActivity.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(RegisterPage.this, "Please Make Sure the Password Inserted is Correct", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterPage.this, "Please make sure the input follow the format ", Toast.LENGTH_SHORT).show();
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
                Intent intent= new Intent(RegisterPage.this,MainActivity.class);
                startActivity(intent);

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

    //This method is to check whether the user input match the gmail format
    public boolean matchEmailFormat(String str){
        Pattern p= Pattern.compile("[a-zA-Z0-9]+@gmail.com");
        Matcher m= p.matcher(str);
        return m.matches();
    }

    private void sendMail(String user) {

        String mail = Email.getText().toString().trim();
        String message = "Dear "+user+",\n\nThank You for choosing GREB!\n\nWe are very happy to welcome you as a registered customer of GREB.\nWe look forward to first journey with us.\nIf you need any assistance please contact grebcustomerservice2156@gmail.com\n\nBest Regards,\nGREB Customer Center";
        String subject = "Greb User Registration";

        //Send Mail
        JavaMailAPI javaMailAPI = new JavaMailAPI(this,mail,subject,message);

        javaMailAPI.execute();

    }

    @Override
    public void onBackPressed(){
        finish();
        Intent intent= new Intent(RegisterPage.this,MainActivity.class);
        startActivity(intent);
    }


}