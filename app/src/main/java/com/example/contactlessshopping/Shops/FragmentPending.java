package com.example.contactlessshopping.Shops;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.HashMap;
import java.util.Map;

public class FragmentPending extends Fragment {
    View view;

    private RecyclerView recyclerView;
    FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("orders");
    private OrderAdapater adapter;

    public FragmentPending(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pending_request, container, false);

        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        Query query = notebookRef.whereEqualTo("status", "0");

        FirestoreRecyclerOptions<OrderModel> options = new FirestoreRecyclerOptions.Builder<OrderModel>()
                .setQuery(query, OrderModel.class)
                .build();

        adapter = new OrderAdapater(options);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OrderAdapater.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
//                OrderModel note = documentSnapshot.toObject(OrderModel.class);
//                String id = documentSnapshot.getId();
//                String path = documentSnapshot.getReference().getPath();
//
//                Map<String, Object> noticeIDUpdate = new HashMap<>();
//                noticeIDUpdate.put("noticeID", id);
//                db.collection("notices").document(id).update(noticeIDUpdate);

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
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
