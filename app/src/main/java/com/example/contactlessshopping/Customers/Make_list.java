package com.example.contactlessshopping.Customers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class Make_list extends AppCompatActivity {

    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    private FirebaseAuth auth;

    String cust_name,shop_id,order_id,order_no;

    DocumentReference docref;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference orderref = db.collection("orders");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makelist);
        auth = FirebaseAuth.getInstance();
        final Intent intent = getIntent();
        shop_id=intent.getStringExtra("shop_id");

        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        order_no= String.format("%05d", num);


        Button btn = (Button) findViewById(R.id.btnAdd);
        Button save = (Button)findViewById(R.id.save);

        list = (ListView) findViewById(R.id.list);
        arrayList = new ArrayList<String>();
        final EditText edit = (EditText)findViewById(R.id.txtItem);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.add(edit.getText().toString());
                adapter.notifyDataSetChanged();
                edit.setText("");
            }
        });
        edit.setText("");


        docref = db.collection("customers").document(auth.getUid());
        docref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            cust_name = documentSnapshot.getString("Name");
                            Toast.makeText(Make_list.this,cust_name,Toast.LENGTH_SHORT).show();

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
                note.put("customer_name",cust_name );
                note.put("shop_id",shop_id);
                note.put("status",0);
                note.put("time_stamp",format);
                note.put("product",arrayList);
                note.put("order_no",order_no);


                db.collection("orders").add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("iiii iiiii iiiiiii ii", "DocumentSnapshot written with ID: " + documentReference.getId());
                        order_id=documentReference.getId();
                        Toast.makeText(Make_list.this,order_id,Toast.LENGTH_SHORT).show();
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




                Intent intent=new Intent(Make_list.this, ShopDetails.class);

                startActivity(intent);
            }
        });

}
}
