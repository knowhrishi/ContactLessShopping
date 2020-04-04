package com.example.contactlessshopping.Customers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.contactlessshopping.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class profile_customer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_customer);

        //nav
        BottomNavigationView bottomNavigationView= findViewById(R.id.nav);

        bottomNavigationView.setSelectedItemId(R.id.item3);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.item1:
                        startActivity(new Intent(getApplicationContext(),Customer_MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.item2:
                        startActivity(new Intent(getApplicationContext(),ManageOrders.class));
                        overridePendingTransition(0,0);
                        return true;

                        case R.id.item3:

                        return true;
                }
                return false;
            }
        });
    }
}
