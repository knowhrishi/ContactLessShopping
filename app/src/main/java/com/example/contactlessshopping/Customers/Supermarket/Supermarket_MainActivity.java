package com.example.contactlessshopping.Customers.Supermarket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.contactlessshopping.Customers.Customer_MainActivity;
import com.example.contactlessshopping.Customers.ShopDetails;
import com.example.contactlessshopping.Customers.ShopsAdapter;
import com.example.contactlessshopping.Customers.Shopsclass;
import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.Main.OrderAdapterPending;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Supermarket_MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView shoplist;
    private supermarketadapter adapter;
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
        setContentView(R.layout.activity_supermarket__main);

        shoplist=findViewById(R.id.shop_list);
        progressBar=findViewById(R.id.progress_bar);


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

        Query query = db.collection("shops").whereEqualTo("shop_category","Supermarket");

        FirestoreRecyclerOptions<Shopsclass> response = new FirestoreRecyclerOptions.Builder<Shopsclass>()
                .setQuery(query, Shopsclass.class)
                .build();
        adapter=new supermarketadapter(response,dlat,dlon, Supermarket_MainActivity.this);

        adapter.notifyDataSetChanged();
        shoplist.setAdapter(adapter);


        adapter.setOnItemClickListener(new OrderAdapterPending.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Intent intent = new Intent(Supermarket_MainActivity.this, supermarket_details.class);
                //intent.putExtra("intendListImageUrl", documentSnapshot.get("url").toString());
                intent.putExtra("shop_name", documentSnapshot.get("shop_name").toString());
                intent.putExtra("shop_id",documentSnapshot.getId());
                Log.d("ID : ",documentSnapshot.getId());
                Toast.makeText(Supermarket_MainActivity.this,documentSnapshot.get("shop_name").toString()+" data sent ",Toast.LENGTH_SHORT).show();
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
