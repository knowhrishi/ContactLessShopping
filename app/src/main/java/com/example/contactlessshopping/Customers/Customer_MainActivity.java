package com.example.contactlessshopping.Customers;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.Main.OrderAdapterPending;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Customer_MainActivity extends AppCompatActivity {

   ProgressBar progressBar;
    RecyclerView  shoplist;
    private ShopsAdapter adapter;
    private FirebaseFirestore db;
    double dlat, dlon;
    String slat,slon;
    LinearLayoutManager gridLayoutManager;

//  MeowBottomNavigation meowBottomNavigation;
    private final static int ID_LIST=1;
    private final static int ID_ORDERS=2;
    private final static int ID_PROFILE=3;

    Button supermarket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__main);
        shoplist=findViewById(R.id.shop_list);
        progressBar=findViewById(R.id.progress_bar);
        //supermarket=findViewById(R.id.supermarket);


        final Intent intent = getIntent();
        slat = intent.getStringExtra("intendLatitude");
        slon = intent.getStringExtra("intentLongitude");
        dlat = Double.parseDouble(slat);
        dlon = Double.parseDouble(slon);

        init();
        getshopList();

        BottomNavigationView bottomNavigationView= findViewById(R.id.nav);

        bottomNavigationView.setSelectedItemId(R.id.item1);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.item1:

                        return true;

                    case R.id.item2:
                        startActivity(new Intent(getApplicationContext(), ManageOrders.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.item3:
                        startActivity(new Intent(getApplicationContext(), profile_customer.class));
                        overridePendingTransition(0,0);
                        return true;


                }
                return false;
            }
        });
    }

    private void init(){

        gridLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        shoplist.setLayoutManager(gridLayoutManager);
        db = FirebaseFirestore.getInstance();
    }

    public void manage(View v)
    {
        Intent i=new Intent(this, ManageOrders.class);
        startActivity(i);
    }
    private void getshopList(){

        Query query = db.collection("shops").whereEqualTo("shop_category","Grocery Store");

        FirestoreRecyclerOptions<Shopsclass> response = new FirestoreRecyclerOptions.Builder<Shopsclass>()
                .setQuery(query, Shopsclass.class)
                .build();
        adapter=new ShopsAdapter(response,dlat,dlon, Customer_MainActivity.this);

        adapter.notifyDataSetChanged();
        shoplist.setAdapter(adapter);


        adapter.setOnItemClickListener(new OrderAdapterPending.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Intent intent = new Intent(Customer_MainActivity.this, ShopDetails.class);
                //intent.putExtra("intendListImageUrl", documentSnapshot.get("url").toString());
                intent.putExtra("shop_name", documentSnapshot.get("shop_name").toString());
                Toast.makeText(Customer_MainActivity.this,documentSnapshot.get("shop_name").toString()+" data sent ",Toast.LENGTH_SHORT).show();
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
