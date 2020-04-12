package com.example.contactlessshopping.Customers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactlessshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Customer_survey extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    RadioGroup availability,service,sdm;
    RadioButton radio1,radio2,radio3;
    Button submit;

    int rating;
    String order_id,customer_name,shop_id,order_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_survey);

        Intent i=getIntent();
        order_id=i.getStringExtra("order_id");
        auth=FirebaseAuth.getInstance();



        submit=findViewById(R.id.submit);
        availability=findViewById(R.id.radioavailability);
        service=findViewById(R.id.radioservice);
        sdm=findViewById(R.id.radiosdm);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedavail = availability.getCheckedRadioButtonId();
                int selectedservice = service.getCheckedRadioButtonId();
                int selectedsdm = sdm.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radio1 = (RadioButton) findViewById(selectedavail);
                radio2 = (RadioButton) findViewById(selectedavail);
                radio3 = (RadioButton) findViewById(selectedavail);
                int total=Integer.parseInt(radio1.getText().toString())+Integer.parseInt(radio2.getText().toString())+Integer.parseInt(radio3.getText().toString());
                rating=total/3;

                db.collection("orders").document(order_id).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists())
                                    {
                                        customer_name=document.get("customer_name").toString();
                                        shop_id=document.get("shop_id").toString();
                                        order_id=document.get("order_id").toString();
                                        order_no=document.get("order_no").toString();

                                        Map<String,Object> rate=new HashMap<>();
                                        rate.put("customer_name",customer_name);
                                        rate.put("shop_id",shop_id);
                                        rate.put("order_id",order_id);
                                        rate.put("order_no",order_no);
                                        rate.put("rating",rating);
                                        db.collection("ratings").add(rate)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d("", "Feedback Noted: " + documentReference.getId());
                                                        Toast.makeText(Customer_survey.this,"Feedback Noted",Toast.LENGTH_SHORT).show();
                                                        Intent i=new Intent(Customer_survey.this, customer_dash.class);
                                                        startActivity(i);

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("Tag: ", "Cannot record", e);
                                                    }
                                                });



                                    }
                                    else {
                                        Log.d("Tag", "No such document");
                                    }
                                } else {
                                    Log.d("Tag", "get failed with ", task.getException());
                                }
                            }
                        });





            }
        });





    }
}
