package com.example.contactlessshopping.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.Main.OrderModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ManageOrders extends AppCompatActivity {
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseAuth auth;
    DocumentReference docref;
    String cust_name;

    ProgressBar progressBar;
    RecyclerView orderlist;
    LinearLayoutManager linearLayoutManager;

    private CollectionReference notebookRef = db.collection("orders");
    private OrdersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        orderlist=findViewById(R.id.order_list);
        //progressBar=findViewById(R.id.progress_bar_order);

        auth=FirebaseAuth.getInstance();

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        orderlist.setLayoutManager(linearLayoutManager);

        docref = db.collection("customers").document(auth.getUid());
        docref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            cust_name = documentSnapshot.getString("Name");
                            Toast.makeText(ManageOrders.this,cust_name,Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


       Query query= notebookRef.whereEqualTo("status", "1");

        FirestoreRecyclerOptions<OrderModel> options = new FirestoreRecyclerOptions.Builder<OrderModel>()
                .setQuery(query, OrderModel.class)
                .build();
        Toast.makeText(this,"built successfully",Toast.LENGTH_SHORT).show();
        adapter=new OrdersAdapter(options);
        adapter.notifyDataSetChanged();
        orderlist.setAdapter(adapter);

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
