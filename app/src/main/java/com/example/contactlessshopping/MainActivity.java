package com.example.contactlessshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.contactlessshopping.Customers.Login_customer;
import com.example.contactlessshopping.Shops.ShopLogin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button shop=(Button)findViewById(R.id.Shop);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Shop login","redirecting");
                Intent intent = new Intent(MainActivity.this, ShopLogin.class);
                startActivity(intent);
            }
        });

        Button customer=(Button)findViewById(R.id.customer);
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Login_customer.class);
                startActivity(intent);
            }
        });

    }
}