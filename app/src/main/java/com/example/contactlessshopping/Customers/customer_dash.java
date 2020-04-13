package com.example.contactlessshopping.Customers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlessshopping.Customers.Fishmarket.Fishmarket_MainActivity;
import com.example.contactlessshopping.Customers.Medical.Medical_MainActivity;
import com.example.contactlessshopping.Customers.Supermarket.Supermarket_MainActivity;
import com.example.contactlessshopping.Customers.Vegetable.Vegetable_MainActivity;
import com.example.contactlessshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class customer_dash extends AppCompatActivity {


    ProgressBar progressBar;
    RecyclerView shoplist;
    private ShopsAdapter adapter;
    double dlat, dlon;
    String slat,slon;
    TextView textViewwelcomemsg;
    // MeowBottomNavigation meowBottomNavigation;
    private final static int ID_LIST=1;
    private final static int ID_ORDERS=2;
    private final static int ID_PROFILE=3;

    LinearLayout linearLayout;

    GridLayout gridLayout;
    private ArrayList<HashMap<String, Object>> maplist = new ArrayList<>();



    private FirebaseAuth auth;
    private DocumentReference noteRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("customer");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dash);

        textViewwelcomemsg = (TextView)findViewById(R.id.welcomemsg);

        final Intent intent = getIntent();
        slat = intent.getStringExtra("intendLatitude");
        slon = intent.getStringExtra("intentLongitude");
        //dlat = Double.parseDouble(slat);
        //dlon = Double.parseDouble(slon);

        gridLayout= (GridLayout) findViewById(R.id.mainGrid);
        setSingleEvent(gridLayout);



        notebookRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Toast.makeText(customer_dash.this,"in complete",Toast.LENGTH_SHORT).show();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Tag", document.getId() + " => " + document.getData());
                                //Toast.makeText(customer_dash.this,document.get("Name").toString(),Toast.LENGTH_SHORT).show();
                                String name=document.get("Name").toString();
                                textViewwelcomemsg.setText("Hi "+name);


                            }
                        } else {
                            Log.d("Tag", "Error getting documents: ", task.getException());
                        }
                    }
                });



        BottomNavigationView bottomNavigationView= findViewById(R.id.nav);
        bottomNavigationView.setSelectedItemId(R.id.item1);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item1:
                        startActivity(new Intent(getApplicationContext(), customer_dash.class));
                        overridePendingTransition(0,0);
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
