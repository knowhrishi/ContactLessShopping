package com.example.contactlessshopping.Shops.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactlessshopping.Customers.Make_list;
import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.ShopMainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {

    TextView textViewCustName, textViewOrderNo, textViewStatus, textViewTime;
    Button buttonAccept, buttonReject;
    ListView listview;
    ArrayList arrayList;
    List lstr;

    private ArrayAdapter<String> adapter;


    public static final String KEY_ORDER_STATUS = "status";
    private static final String TAG = "OrderDetails";
    String order_status;

    private FirebaseAuth auth;
    private DocumentReference noteRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("orders");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        textViewCustName = (TextView) findViewById(R.id.idCustomerName);
        textViewOrderNo = (TextView) findViewById(R.id.idOrderNo);
        textViewStatus = (TextView) findViewById(R.id.idOrderStatus);
        textViewTime = (TextView) findViewById(R.id.idOrderTimeStamp);
        buttonAccept = (Button) findViewById(R.id.idBtnAccept);
        buttonReject = (Button) findViewById(R.id.idBtnReject);
        listview=findViewById(R.id.list_order);



        arrayList = new ArrayList<String>();
        //arrayList.add("first");



        auth = FirebaseAuth.getInstance();



        final Intent intent = getIntent();
        textViewCustName.setText(intent.getStringExtra("intentCustomerName").toUpperCase());
        textViewOrderNo.setText(intent.getStringExtra("intentOrderNo").toUpperCase());
        textViewStatus.setText("PENDING");
        textViewTime.setText(intent.getStringExtra("intentOrderTimeStamp").toUpperCase());




        final String id = intent.getStringExtra("intentOrderId");


        DocumentReference orderref = db.collection("orders").document(id);
        orderref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {


                    List<String> mylist = (List<String>) documentSnapshot.get("product");

                    //Toast.makeText(OrderDetails.this,mylist.toString(),Toast.LENGTH_SHORT).show();
                    if(mylist.size()!=0) {

                        arrayList = new ArrayList<String>(mylist);
                        Toast.makeText(OrderDetails.this, arrayList.toString() + "arraylist", Toast.LENGTH_SHORT).show();
                        adapter = new ArrayAdapter<String>(OrderDetails.this, android.R.layout.simple_list_item_1, arrayList);
                        listview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }


                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });





        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_status = "1";
                DocumentReference orderRefAccept = db.collection("orders").document(id);
                orderRefAccept.update(KEY_ORDER_STATUS,order_status);



                startActivity(new Intent(OrderDetails.this, ShopMainActivity.class));
                finish();




            }
        });


        buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_status = "2";
                DocumentReference orderRefRej = db.collection("orders").document(id);
                orderRefRej.update(KEY_ORDER_STATUS,order_status);
                startActivity(new Intent(OrderDetails.this, ShopMainActivity.class));
                finish();
            }
        });


    }
}
