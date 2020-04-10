package com.example.contactlessshopping.Customers.Supermarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactlessshopping.Customers.ShopDetails;
import com.example.contactlessshopping.Customers.Upload_list;
import com.example.contactlessshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.example.contactlessshopping.Shops.Main.OrderDetails.KEY_ORDER_STATUS;

public class supermarket_details extends AppCompatActivity {
    private FirebaseAuth auth;
    private DocumentReference noteRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("tokens");


    String shop_id,shop_name,capacity,token_no;

    Button get_token;
    TextView token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket_details);
        auth=FirebaseAuth.getInstance();

        final Intent intent=getIntent();
        shop_id=intent.getStringExtra("shop_id");
        shop_name=intent.getStringExtra("shop_name");

        get_token=findViewById(R.id.get_token);
        token=findViewById(R.id.token_no);
        get_token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("tokens").document(shop_id).get()
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
                                            if(mylist.size()!=10)
                                            {

                                                DocumentReference orderRefAccept = db.collection("tokens").document(shop_id);
                                                orderRefAccept.update(i, FieldValue.arrayUnion(token_no));
                                                Toast.makeText(supermarket_details.this,i+" slot is allocated to you!!",Toast.LENGTH_SHORT).show();
                                                Map<String,Object> map=new HashMap<>();
                                                map.put("token_no",token_no);
                                                Toast.makeText(supermarket_details.this,auth.getUid().toString(),Toast.LENGTH_SHORT).show();
                                                db.collection("customers").document(auth.getUid().toString()).update("token_supermarket",token_no);
                                                Toast.makeText(supermarket_details.this,token_no,Toast.LENGTH_SHORT).show();
                                                token.setText(token_no);
                                                break;

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

    }
}