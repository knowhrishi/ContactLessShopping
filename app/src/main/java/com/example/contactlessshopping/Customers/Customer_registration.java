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

import com.example.contactlessshopping.MainActivity;
import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.ShopRegistration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Customer_registration extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText emailid,password,name,custno;
    String sname,semail,spass,scustno;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);

        emailid=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.pass);
        name=(EditText)findViewById(R.id.name);
        custno=(EditText)findViewById(R.id.custno);
        submit=(Button)findViewById(R.id.submitbt);



// ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                 semail=emailid.getText().toString();
                 spass=password.getText().toString();
                 sname=name.getText().toString();
                 scustno=custno.getText().toString();

                if (!TextUtils.isEmpty(semail) && !TextUtils.isEmpty(spass) &&!TextUtils.isEmpty(sname) &&!TextUtils.isEmpty(scustno)) {
                    createuser(semail, spass);
                } else {

                    Toast.makeText(Customer_registration.this, "Empty Inputs are not allowed", Toast.LENGTH_SHORT).show();
                }



            }
        });





    }

    private void createuser(final String emailid, String password)
    {
        Log.d("in onclick","successfull on click");



        mAuth.createUserWithEmailAndPassword(emailid, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Map<String, Object> note = new HashMap<>();
                            note.put("Name", sname);
                            note.put("emailid",semail);
                            note.put("Customer_no",scustno);

                            db.collection("customers").document(mAuth.getUid()).set(note)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Customer_registration.this, "Data saved", Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //Toast.makeText(PatientRegister.this, "Error!", Toast.LENGTH_SHORT).show();
                                            Log.d("log", e.toString());
                                        }
                                    });


                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Successfull:", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent=new Intent(Customer_registration.this,Login_customer.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Unsuccessfull", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Customer_registration.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });


    }
}
