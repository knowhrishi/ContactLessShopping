package com.example.contactlessshopping.Shops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contactlessshopping.MainActivity;
import com.example.contactlessshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ShopLogin extends AppCompatActivity {


    private EditText editTextEmail, editTextPassword;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private Button btnSignup, btnLogin, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_login);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(ShopLogin.this, ShopRegistration.class));
            finish();
        }

        editTextEmail      = (EditText) findViewById(R.id.email);
        editTextPassword   = (EditText) findViewById(R.id.password);
        progressDialog = new ProgressDialog(this);
        btnSignup          = (Button) findViewById(R.id.btn_signup);
        btnLogin           = (Button) findViewById(R.id.btn_login);
        //btnReset           = (Button) findViewById(R.id.btn_reset_password);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopLogin.this, ShopRegistration.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                final String password = editTextPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(ShopLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        editTextPassword.setError("Password should be greater than 6 digits");
                                    }else {
                                        Toast.makeText(ShopLogin.this, "Auth Fail", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(ShopLogin.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}
