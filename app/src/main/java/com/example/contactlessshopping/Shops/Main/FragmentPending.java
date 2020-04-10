package com.example.contactlessshopping.Shops.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlessshopping.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FragmentPending extends Fragment {
    View view;

    private RecyclerView recyclerView;
    FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("orders");
    private OrderAdapterPending adapterPending;

    public FragmentPending(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pending_request, container, false);
        auth = FirebaseAuth.getInstance();

        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        Query query = notebookRef.whereEqualTo("shop_id",auth.getUid()).whereEqualTo("status", "0");

        FirestoreRecyclerOptions<OrderModel> options = new FirestoreRecyclerOptions.Builder<OrderModel>()
                .setQuery(query, OrderModel.class)
                .build();

        adapterPending = new OrderAdapterPending(options);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapterPending);

        adapterPending.setOnItemClickListener(new OrderAdapterPending.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Intent intent = new Intent(getActivity(), OrderDetails.class);
                //intent.putExtra("intendListImageUrl", documentSnapshot.get("url").toString());
                intent.putExtra("intentOrderNo", documentSnapshot.get("order_no").toString());
                intent.putExtra("intentCustomerName", documentSnapshot.get("customer_name").toString());
                intent.putExtra("intentStatus", documentSnapshot.get("status").toString());
                intent.putExtra("intentOrderTimeStamp", documentSnapshot.get("timestamp").toString());
                intent.putExtra("intentOrderId", documentSnapshot.get("order_id").toString());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);


            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        adapterPending.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterPending.stopListening();
    }
}
