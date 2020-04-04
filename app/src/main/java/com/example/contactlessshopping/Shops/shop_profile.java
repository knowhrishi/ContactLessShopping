package com.example.contactlessshopping.Shops;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.contactlessshopping.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class shop_profile extends AppCompatActivity {

    TextView name,number,email,address,open,close,capacity;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;
    Button edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile);

        name=findViewById(R.id.shop_name);
        number=findViewById(R.id.shop_number);
        email=findViewById(R.id.shop_email);
        address=findViewById(R.id.shop_address);
        open=findViewById(R.id.shop_open);
        close=findViewById(R.id.shop_close);
        capacity=findViewById(R.id.shop_capacity);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        userID= firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference =firebaseFirestore.collection("shops").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("shop_name"));
                number.setText(documentSnapshot.getString("phone_number"));
                email.setText(documentSnapshot.getString("email_id"));
                address.setText(documentSnapshot.getString("address"));
                open.setText(documentSnapshot.getString("from_time"));
                close.setText(documentSnapshot.getString("to_time"));
                capacity.setText(documentSnapshot.getString("capacity"));
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(shop_profile.this,shop_edit_profile.class);
                startActivity(intent);
            }
        });
    }
}
