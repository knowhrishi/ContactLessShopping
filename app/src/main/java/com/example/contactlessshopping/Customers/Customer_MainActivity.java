package com.example.contactlessshopping.Customers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactlessshopping.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class Customer_MainActivity extends AppCompatActivity {


   ProgressBar progressBar;


    RecyclerView  shoplist;

    private FirebaseFirestore db;
    private FirestoreRecyclerAdapter adapter;
    LinearLayoutManager gridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__main);
        shoplist=findViewById(R.id.shop_list);
        progressBar=findViewById(R.id.progress_bar);

        init();
        getshopList();
    }

    private void init(){

        gridLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        shoplist.setLayoutManager(gridLayoutManager);
        db = FirebaseFirestore.getInstance();
    }


    public class ShopsHolder extends RecyclerView.ViewHolder {
        private View view;


        TextView textName,textTitle,textCompany;

        public ShopsHolder(View itemView) {
            super(itemView);
            view=itemView;


            textName=(TextView)view.findViewById(R.id.name_shop);

            textTitle=(TextView)view.findViewById(R.id.from);

             textCompany=(TextView)view.findViewById(R.id.to);

        }
    }




    private void getshopList(){
        Query query = db.collection("shops");

        FirestoreRecyclerOptions<Shopsclass> response = new FirestoreRecyclerOptions.Builder<Shopsclass>()
                .setQuery(query, Shopsclass.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Shopsclass, ShopsHolder>(response) {
            @Override
            public void onBindViewHolder(ShopsHolder holder, int position, Shopsclass model) {
                progressBar.setVisibility(View.GONE);
                holder.textName.setText(model.getshop_name());
                holder.textTitle.setText(model.getfrom_time());
                holder.textCompany.setText(model.getto_time());

            }

            @Override
            public ShopsHolder onCreateViewHolder(ViewGroup group, int i) {
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.list_item_shops, group, false);

                return new ShopsHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };

        adapter.notifyDataSetChanged();
        shoplist.setAdapter(adapter);
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
