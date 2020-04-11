package com.example.contactlessshopping.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.contactlessshopping.Customers.Fishmarket.Fishmarket_MainActivity;
import com.example.contactlessshopping.Customers.Medical.Medical_MainActivity;
import com.example.contactlessshopping.Customers.Supermarket.Supermarket_MainActivity;
import com.example.contactlessshopping.Customers.Vegetable.Vegetable_MainActivity;
import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.Main.OrderAdapterPending;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactlessshopping.Customers.Supermarket.Supermarket_MainActivity;
import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.Main.OrderAdapterPending;
import com.example.contactlessshopping.Shops.ShopRegistration;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.os.Build.VERSION_CODES.M;

public class customer_dash extends AppCompatActivity {


    ProgressBar progressBar;
    RecyclerView shoplist;
    private ShopsAdapter adapter;
    private FirebaseFirestore db;
    double dlat, dlon;
    String slat,slon;
    LinearLayoutManager gridLayoutManager;

    // MeowBottomNavigation meowBottomNavigation;
    private final static int ID_LIST=1;
    private final static int ID_ORDERS=2;
    private final static int ID_PROFILE=3;

    GridLayout gridLayout;
    private ArrayList<HashMap<String, Object>> maplist = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dash);


        final Intent intent = getIntent();
        slat = intent.getStringExtra("intendLatitude");
        slon = intent.getStringExtra("intentLongitude");
        //dlat = Double.parseDouble(slat);
        //dlon = Double.parseDouble(slon);

        gridLayout= (GridLayout) findViewById(R.id.mainGrid);
        setSingleEvent(gridLayout);

        BottomNavigationView bottomNavigationView= findViewById(R.id.nav);
        bottomNavigationView.setSelectedItemId(R.id.item1);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item1:
                        return true;

                    case R.id.item2:
                        startActivity(new Intent(getApplicationContext(),ManageOrders.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.item3:
                        startActivity(new Intent(getApplicationContext(),profile_customer.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }


    private void setSingleEvent(GridLayout gridLayout) {
        for(int i = 0; i<gridLayout.getChildCount();i++){
            CardView cardView=(CardView)gridLayout.getChildAt(i);
            final int finalI= i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(customer_dash.this,"Clicked at index "+ finalI, Toast.LENGTH_SHORT).show();

                    switch (finalI) {
                        case 0:
                            Intent intent = new Intent(getApplicationContext(), Customer_MainActivity.class);
                            intent.putExtra("intendLatitude", slat);
                            intent.putExtra("intentLongitude", slon);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(intent, 0);
                            break;
                        case 1:
                            Intent i2 = new Intent(getApplicationContext(), Supermarket_MainActivity.class);
                            i2.putExtra("intendLatitude", slat);
                            i2.putExtra("intentLongitude", slon);
                            i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(i2, 0);
                            break;
                        case 2:
                            Intent i3 = new Intent(getApplicationContext(), Medical_MainActivity.class);
                            i3.putExtra("intendLatitude", slat);
                            i3.putExtra("intentLongitude", slon);
                            i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(i3, 0);
                            break;
                        case 3:
                            Intent i4 = new Intent(getApplicationContext(), Vegetable_MainActivity.class);
                            i4.putExtra("intendLatitude", slat);
                            i4.putExtra("intentLongitude", slon);
                            i4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivityForResult(i4, 0);
                            break;
                        case 4:
                            Intent i=new Intent(customer_dash.this, Fishmarket_MainActivity.class);
                            i.putExtra("intendLatitude", slat);
                            i.putExtra("intentLongitude", slon);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            break;
                        default:
                    }
                }
            });
        }
    }

}
