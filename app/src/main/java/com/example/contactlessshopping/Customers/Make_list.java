package com.example.contactlessshopping.Customers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactlessshopping.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class
Make_list extends AppCompatActivity {

    private ListView list;
    private FirebaseAuth auth;

    AutoCompleteTextView textIn;
    Button buttonAdd;
    LinearLayout container;
    ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    String cust_name, shop_id, order_id, order_no, shop_name;

    DocumentReference docref;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference orderref = db.collection("orders");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makelist);
        auth = FirebaseAuth.getInstance();
        final Intent intent = getIntent();
        shop_id = intent.getStringExtra("shop_id");
        shop_name = intent.getStringExtra("shop_name");


        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        order_no = String.format("%05d", num);

        Button save = (Button) findViewById(R.id.save);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayList);
        textIn = (AutoCompleteTextView)findViewById(R.id.textin);
        textIn.setAdapter(adapter);
        buttonAdd = (Button)findViewById(R.id.add);
        container = (LinearLayout) findViewById(R.id.container);
        arrayList = new ArrayList<String>();


        buttonAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View addView = layoutInflater.inflate(R.layout.row, null);
                final AutoCompleteTextView textOut = (AutoCompleteTextView)addView.findViewById(R.id.textout);

                textOut.setAdapter(adapter);
                textOut.setText(textIn.getText().toString());
                arrayList.add(textIn.getText().toString());

                Toast.makeText(getApplicationContext(), "Added : "+textIn.getText().toString(), Toast.LENGTH_SHORT).show();

                Button buttonRemove = (Button)addView.findViewById(R.id.remove);
                final View.OnClickListener thisListener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        ((LinearLayout)addView.getParent()).removeView(addView);
                        arrayList.remove(textOut.getText().toString());
                        Toast.makeText(getApplicationContext(), "Removed : "+textOut.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                };
                buttonRemove.setOnClickListener(thisListener);
                container.addView(addView);
            }
        });


        docref = db.collection("customers").document(auth.getUid());
        docref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            cust_name = documentSnapshot.getString("Name");
                            Toast.makeText(Make_list.this, cust_name, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        final String format = simpleDateFormat.format(new Date());
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Data Saved !", Toast.LENGTH_SHORT).show();
                OrderAdapter odr = new OrderAdapter(arrayList);

                Map<String, Object> note = new HashMap<>();
                note.put("customer_name", cust_name);
                note.put("shop_id", shop_id);
                note.put("shop_name", shop_name);
                note.put("status", "0");
                note.put("timestamp", format);
                note.put("product", arrayList);
                note.put("order_no", order_no);


                db.collection("orders").add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("iiii iiiii iiiiiii ii", "DocumentSnapshot written with ID: " + documentReference.getId());
                        order_id = documentReference.getId();
                        Toast.makeText(Make_list.this, order_id, Toast.LENGTH_SHORT).show();
                        Map<String, Object> data = new HashMap<>();
                        data.put("order_id", order_id);

                        db.collection("orders").document(order_id).set(data, SetOptions.merge());


                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("", "Error adding document", e);
                            }
                        });

                /*
                 */


                Intent intent = new Intent(Make_list.this, ShopDetails.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
