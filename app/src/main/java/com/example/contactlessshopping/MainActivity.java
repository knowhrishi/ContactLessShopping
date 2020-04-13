package com.example.contactlessshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.contactlessshopping.Customers.Login_customer;
import com.example.contactlessshopping.Shops.Main.ShopLogin;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }

        Button shop=(Button)findViewById(R.id.sale);
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Shop login","redirecting");
                Intent intent = new Intent(MainActivity.this, ShopLogin.class);
                startActivity(intent);
            }
        });

        Button customer=(Button)findViewById(R.id.buy);

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Login_customer.class);
                startActivity(intent);
            }
        });

    }


}