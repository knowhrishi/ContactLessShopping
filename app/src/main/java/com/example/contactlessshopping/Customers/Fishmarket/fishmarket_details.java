package com.example.contactlessshopping.Customers.Fishmarket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.contactlessshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class fishmarket_details extends AppCompatActivity {
    private FirebaseAuth auth;
    private DocumentReference noteRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("tokens");


    String shop_id,shop_name,capacity,token_no;

    Button get_token;
    TextView token,slot,shop;
    CardView card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishmarket_details);
        auth=FirebaseAuth.getInstance();

        final Intent intent=getIntent();
        shop_id=intent.getStringExtra("shop_id");
        shop_name=intent.getStringExtra("shop_name");

        get_token=findViewById(R.id.get_token2);
        token=findViewById(R.id.token_no2);
        get_token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("token_slots").document(shop_id).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d("Tag", "DocumentSnapshot data: " + document.getData());
                                        Map<String, Object> data = document.getData();
                                        data.remove("date");
                                        data.remove("shop_id");
                                        Log.d("Tag", "DocumentSnapshot keyset: " + data.keySet());
                                        Log.d("Tag", "DocumentSnapshot entryset: " + data.entrySet());

                                        SecureRandom random = new SecureRandom();
                                        int num = random.nextInt(100000);
                                        token_no= String.format("%05d", num);

                                        List<String> keys = new ArrayList<String>(data.keySet());
                                        Collections.sort(keys);

                                        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                                        Log.d("time",currentTime);
                                        Log.d("Tag", " keyset sorted : " + keys);
                                        for(String i :keys)
                                        {


                                            List<String> mylist =(List<String>)data.get(i);


                                            String[] s = i.split("-");
                                            Date slot_from=new Date();
                                            Date slot_to=new Date();
                                            Date currtime=new Date();

                                            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                                            try {
                                                slot_from = dateFormat.parse(s[0].trim());
                                                slot_to=dateFormat.parse(s[1].trim());
                                                currtime=dateFormat.parse(currentTime.trim());
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }



                                            if(slot_from.before(currtime) && currtime.before(slot_to) )
                                            {
                                                if (mylist.size() != 10) {

                                                    DocumentReference orderRefAccept = db.collection("token_slots").document(shop_id);
                                                    orderRefAccept.update(i, FieldValue.arrayUnion(auth.getUid()));
                                                    Toast.makeText(fishmarket_details.this, i + " slot is allocated to you!!", Toast.LENGTH_SHORT).show();

                                                    Toast.makeText(fishmarket_details.this, auth.getUid().toString(), Toast.LENGTH_SHORT).show();

                                                    Map<String,Object> token_doc=new HashMap<>();
                                                    token_doc.put("shop_id",shop_id);
                                                    token_doc.put("token_no",token_no);
                                                    token_doc.put("customer_id",auth.getUid());
                                                    token_doc.put("slot_allocated",i);

                                                    db.collection("tokens").document(auth.getUid().toString()).set(token_doc);
                                                    Toast.makeText(fishmarket_details.this, token_no, Toast.LENGTH_SHORT).show();

                                                    break;

                                                }
                                            }
                                            else if(slot_from.after(currtime))
                                            {
                                                if (mylist.size() != 10) {

                                                    DocumentReference orderRefAccept = db.collection("token_slots").document(shop_id);
                                                    orderRefAccept.update(i, FieldValue.arrayUnion(auth.getUid()));
                                                    Toast.makeText(fishmarket_details.this, i + " slot is allocated to you!!", Toast.LENGTH_SHORT).show();

                                                    Toast.makeText(fishmarket_details.this, auth.getUid().toString(), Toast.LENGTH_SHORT).show();

                                                    Map<String,Object> token_doc=new HashMap<>();
                                                    token_doc.put("shop_id",shop_id);
                                                    token_doc.put("token_no",token_no);
                                                    token_doc.put("customer_id",auth.getUid());
                                                    token_doc.put("slot_allocated",i);

                                                    db.collection("tokens").document(auth.getUid().toString()).set(token_doc);
                                                    Toast.makeText(fishmarket_details.this, token_no, Toast.LENGTH_SHORT).show();

                                                    break;


                                                }

                                            }


                                        }

                                    } else {
                                        Log.d("Tag", "No such document");
                                    }
                                } else {
                                    Log.d("Tag", "get failed with ", task.getException());
                                }
                            }
                        });



            }
        });

        db.collection("tokens").document(auth.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                get_token.setVisibility(View.INVISIBLE);
                                Toast.makeText(fishmarket_details.this,"appointment scheduled",Toast.LENGTH_SHORT).show();
                                token.setText(document.get("token_no").toString());
                                slot.setText(document.get("slot_allocated").toString());
                                shop.setText(shop_name);


                            } else {
                                Log.d("", "No Appointment found");
                                card.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            Log.d("", "get failed with ", task.getException());
                        }
                    }
                });


    }
}