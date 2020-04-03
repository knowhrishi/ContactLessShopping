package com.example.contactlessshopping.Shops;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.contactlessshopping.MainActivity;
import com.example.contactlessshopping.R;

public class shop_dash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_dash);
        Button profile=(Button)findViewById(R.id.profile);
       profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent= new Intent(shop_dash.this, shop_profile.class);
              startActivity(intent);
            }
        });
    }
}
