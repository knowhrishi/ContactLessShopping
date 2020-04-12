package com.example.contactlessshopping.Customers.Fishmarket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlessshopping.Customers.Shopsclass;
import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.Main.OrderAdapterPending;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class Fishmarket_MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView shoplist;
    private fishmarketAdapter adapter;
    private FirebaseFirestore db;
    double dlat, dlon;
    LinearLayoutManager gridLayoutManager;
    //    MeowBottomNavigation meowBottomNavigation;
    private final static int ID_LIST=1;
    private final static int ID_ORDERS=2;
    private final static int ID_PROFILE=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishmarket_main);

        shoplist=findViewById(R.id.shop_list2);
        progressBar=findViewById(R.id.progress_bar2);


        final Intent intent = getIntent();
        String slat = intent.getStringExtra("intendLatitude");
        String slon = intent.getStringExtra("intentLongitude");
        dlat = Double.parseDouble(slat);
        dlon = Double.parseDouble(slon);

        init();
        getshopList();


    }

    private void init(){

        gridLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        shoplist.setLayoutManager(gridLayoutManager);
        db = FirebaseFirestore.getInstance();
    }

    private void getshopList(){

        Query query = db.collection("shops").whereEqualTo("shop_category","FishMarket");

        FirestoreRecyclerOptions<Shopsclass> response = new FirestoreRecyclerOptions.Builder<Shopsclass>()
                .setQuery(query, Shopsclass.class)
                .build();
        adapter=new fishmarketAdapter(response,dlat,dlon, com.example.contactlessshopping.Customers.Fishmarket.Fishmarket_MainActivity.this);
        adapter.notifyDataSetChanged();
        shoplist.setAdapter(adapter);


        adapter.setOnItemClickListener(new OrderAdapterPending.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Intent intent = new Intent(com.example.contactlessshopping.Customers.Fishmarket.Fishmarket_MainActivity.this, fishmarket_details.class);
                //intent.putExtra("intendListImageUrl", documentSnapshot.get("url").toString());
                intent.putExtra("shop_name", documentSnapshot.get("shop_name").toString());
                intent.putExtra("shop_id",documentSnapshot.getId());
                Log.d("ID : ",documentSnapshot.getId());
                Toast.makeText(com.example.contactlessshopping.Customers.Fishmarket.Fishmarket_MainActivity.this,documentSnapshot.get("shop_name").toString()+" data sent ",Toast.LENGTH_SHORT).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


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

