package com.example.contactlessshopping.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.contactlessshopping.MainActivity;
import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.Main.OrderAdapterPending;
import com.example.contactlessshopping.Shops.Main.OrderModel;
import com.example.contactlessshopping.Shops.ShopRegistration;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SlotsMain extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth;
    DocumentReference docref;
    RecyclerView slotsRV;
    LinearLayoutManager linearLayoutManager;
    String order_no;
    private CollectionReference notebookRef = db.collection("slots");
    private SlotsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slots_main);


        auth = FirebaseAuth.getInstance();

        final Intent intent = getIntent();
        final String order_id = intent.getStringExtra("intendorder_id");
        String shop_id = intent.getStringExtra("intendshop_id");
        order_no = intent.getStringExtra("intendorder_no");


        Query query = notebookRef.whereEqualTo("shop_id", shop_id).whereEqualTo("slot_status", "0").orderBy("slot", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<SlotsModel> options = new FirestoreRecyclerOptions.Builder<SlotsModel>()
                .setQuery(query, SlotsModel.class)
                .build();

        adapter = new SlotsAdapter(options);
        slotsRV = (RecyclerView) findViewById(R.id.idSlotsRV);
        slotsRV.setHasFixedSize(true);
        slotsRV.setLayoutManager(new LinearLayoutManager(SlotsMain.this));
        slotsRV.setAdapter(adapter);

        adapter.setOnItemClickListener(new SlotsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, int position) {

                Toast.makeText(SlotsMain.this, order_no + "\n" + "SLOT_ID: " + documentSnapshot.get("slot_id").toString(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(SlotsMain.this);
                builder.setTitle("Book Slot?");
                builder.setMessage("Confirm pickup between " + documentSnapshot.get("slot").toString() + " for order #" + order_no);
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                notebookRef.document(documentSnapshot.get("slot_id").toString()).update("orders", FieldValue.arrayUnion(order_no));
                                DocumentReference orderRefAccept = db.collection("orders").document(order_id);
                                orderRefAccept.update("status", "3");

                                Map<String, Object> note = new HashMap<>();
                                note.put("pickup_slot", documentSnapshot.get("slot").toString());

                                DocumentReference docRef = db.collection("orders").document(order_id);
                                docRef.update("pickup_slot", documentSnapshot.get("slot").toString());
//
                                startActivity(new Intent(SlotsMain.this, ManageOrders.class));
                                finish();
//


                            }
                        });
                builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                android.R.string.no, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }


        });

    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
