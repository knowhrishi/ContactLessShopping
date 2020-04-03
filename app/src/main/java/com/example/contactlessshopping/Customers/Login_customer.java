package com.example.contactlessshopping.Customers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactlessshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_customer extends AppCompatActivity {

    EditText email,password;
    Button submit,signup;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_customer);


        email=(EditText)findViewById(R.id.login_email);
        password=(EditText)findViewById(R.id.login_pass);
        submit=(Button)findViewById(R.id.submit_login);
        mAuth = FirebaseAuth.getInstance();




        signup=(Button)findViewById(R.id.signupbt);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.d("in signup","successfull");

                Intent intent=new Intent(Login_customer.this,Customer_registration.class);
                startActivity(intent);


            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String semail=email.getText().toString();
                String spass=password.getText().toString();

                if (!TextUtils.isEmpty(semail) && !TextUtils.isEmpty(spass)) {
                    loginuser(semail, spass);
                } else {

                    Toast.makeText(Login_customer.this, "Failed Login: Empty Inputs are not allowed", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    private void loginuser(String email,String pass)
    {


        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           // Intent intent=new Intent(Login_customer.this,Customers);
                           // startActivity(intent);
                            Toast.makeText(Login_customer.this,"Successfull Login",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Login_customer.this,Customer_MainActivity.class);
                            startActivity(intent);


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Login_customer.this, "Auth Failed!", Toast.LENGTH_SHORT).show();
                Log.d("tag", e.toString());
                password.getText().clear();
            }
        });





    }
}
