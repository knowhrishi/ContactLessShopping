package com.example.contactlessshopping.Shops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactlessshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {

    TextView textViewCustName, textViewOrderNo, textViewStatus, textViewTime;
    Button buttonAccept, buttonReject;


    public static final String KEY_ORDER_STATUS = "status";
    private static final String TAG = "OrderDetails";
    String order_status;

    private FirebaseAuth auth;
    private DocumentReference noteRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("orders");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        textViewCustName = (TextView) findViewById(R.id.idCustomerName);
        textViewOrderNo = (TextView) findViewById(R.id.idOrderNo);
        textViewStatus = (TextView) findViewById(R.id.idOrderStatus);
        textViewTime = (TextView) findViewById(R.id.idOrderTimeStamp);
        buttonAccept = (Button) findViewById(R.id.idBtnAccept);
        buttonReject = (Button) findViewById(R.id.idBtnReject);

        auth = FirebaseAuth.getInstance();

        final Intent intent = getIntent();
        textViewCustName.setText(intent.getStringExtra("intentCustomerName"));
        textViewOrderNo.setText(intent.getStringExtra("intentOrderNo"));
        textViewStatus.setText("Pending");
        textViewTime.setText(intent.getStringExtra("intentOrderTimeStamp"));
        final String id = intent.getStringExtra("intentOrderId");
        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_status = "1";
                DocumentReference orderRefAccept = db.collection("orders").document(id);
                orderRefAccept.update(KEY_ORDER_STATUS,order_status);
                startActivity(new Intent(OrderDetails.this, ShopMainActivity.class));
                finish();
            }
        });


        buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_status = "2";
                DocumentReference orderRefRej = db.collection("orders").document(id);
                orderRefRej.update(KEY_ORDER_STATUS,order_status);
                startActivity(new Intent(OrderDetails.this, ShopMainActivity.class));
                finish();
            }
        });


    }
}
