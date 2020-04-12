package com.example.contactlessshopping.Customers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactlessshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ShopDetails extends AppCompatActivity {


    TextView name,ph,time;
    String image,shopname,phno,fromtime,totime,id;
    Button imgupload,makelist;


    private FirebaseAuth auth;
    private DocumentReference noteRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("shops");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        final Intent intent = getIntent();
       shopname= intent.getStringExtra("shop_name");
        auth = FirebaseAuth.getInstance();

        name=findViewById(R.id.welcome_text);
        ph=findViewById(R.id.phno);
        time=findViewById(R.id.time);
        name.setText(shopname);

        imgupload=findViewById(R.id.uploadimg);
        makelist=findViewById(R.id.makelist);


        makelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ShopDetails.this,id,Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(), Make_list.class);
                i.putExtra("shop_id",id);
                i.putExtra("shop_name",shopname);
                startActivity(i);
            }
        });

        imgupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), Upload_list.class);
                i.putExtra("shop_id",id);
                startActivity(i);
            }
        });












// future.get() blocks on response
        notebookRef
                .whereEqualTo("shop_name", shopname)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Toast.makeText(ShopDetails.this,"in complete",Toast.LENGTH_SHORT).show();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Tag", document.getId() + " => " + document.getData());
                                Toast.makeText(ShopDetails.this,document.get("capacity").toString(),Toast.LENGTH_SHORT).show();
                                phno=document.get("phone_number").toString();
                                fromtime=document.get("from_time").toString();
                                totime=document.get("to_time").toString();
                                id=document.getId().toString();

                                ph.setText(phno);
                                time.setText(fromtime+" - "+totime);

                            }
                        } else {
                            Log.d("Tag", "Error getting documents: ", task.getException());
                        }
                    }
                });



    }
}
