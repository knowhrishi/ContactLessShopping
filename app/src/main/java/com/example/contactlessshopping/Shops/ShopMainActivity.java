package com.example.contactlessshopping.Shops;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.Main.FragmentAccepted;
import com.example.contactlessshopping.Shops.Main.FragmentDeclined;
import com.example.contactlessshopping.Shops.Main.FragmentPending;
import com.example.contactlessshopping.Shops.Main.ViewPagerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShopMainActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private TextView textViewShopname;
    Button edit;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth;
    private DocumentReference noteRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_main);

        auth = FirebaseAuth.getInstance();

        tabLayout = (TabLayout) findViewById(R.id.idTabLayout);
//        appBarLayout = (AppBarLayout) findViewById(R.id.idAppbar);
        viewPager = (ViewPager) findViewById(R.id.idViewPager);
        textViewShopname = (TextView) findViewById(R.id.idShopName);
        edit=(Button) findViewById(R.id.p_edit);

        ViewPagerAdapter adapter = new ViewPagerAdapter((getSupportFragmentManager()));

        adapter.AddFragment(new FragmentPending(), "All Orders");
        adapter.AddFragment(new FragmentAccepted(), "Accepted");
        adapter.AddFragment(new FragmentDeclined(), "Packed");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        showInfo();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ShopMainActivity.this, shop_profile.class);
                startActivity(intent);
            }
        });
    }

    public void showInfo() {


        noteRef = db.collection("shops").document(auth.getUid());
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String shop_name = documentSnapshot.getString("shop_name");

                            textViewShopname.setText(shop_name);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

}
