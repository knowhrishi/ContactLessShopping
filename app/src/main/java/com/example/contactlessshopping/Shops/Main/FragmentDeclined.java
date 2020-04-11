package com.example.contactlessshopping.Shops.Main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlessshopping.Customers.ManageOrders;
import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.ShopMainActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.security.SecureRandom;

public class FragmentDeclined extends Fragment {
    View view;

    private RecyclerView recyclerView;
    FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("orders");
    private OrderAdapterRejected adapterRejected;

    private DocumentReference noteRef;

    public FragmentDeclined() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_declined_request, container, false);
        setupRecyclerView();
        return view;
    }

    private void setupRecyclerView() {
        Query query = notebookRef.whereEqualTo("status", "2");

        FirestoreRecyclerOptions<OrderModel> options = new FirestoreRecyclerOptions.Builder<OrderModel>()
                .setQuery(query, OrderModel.class)
                .build();

        adapterRejected = new OrderAdapterRejected(options);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapterRejected);


        adapterRejected.setOnItemClickListener(new OrderAdapterRejected.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, int position) {

//                Intent intent = new Intent(getActivity(), OrderDetails.class);
//                //intent.putExtra("intendListImageUrl", documentSnapshot.get("url").toString());
//                intent.putExtra("intentOrderNo", documentSnapshot.get("order_no").toString());
//                intent.putExtra("intentCustomerName", documentSnapshot.get("customer_name").toString());
//                intent.putExtra("intentStatus", documentSnapshot.get("status").toString());
//                intent.putExtra("intentOrderTimeStamp", documentSnapshot.get("timestamp").toString());
//                intent.putExtra("intentOrderId", documentSnapshot.get("order_id").toString());
//
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getActivity().startActivity(intent);


                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                builder.setTitle("Picking up order?");
//                builder.setMessage("Once order accepted you cannot cancel the order.");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface alertDialog,
                                                int which) {
                                final Dialog dialog = new Dialog(getActivity());
                                dialog.setContentView(R.layout.order_pickup_shop_dialog);
                                final TextView textViewIncoorectCode = (TextView) dialog.findViewById(R.id.incorrectcode);
                                final EditText text = (EditText) dialog.findViewById(R.id.oderpickupcode);
                                TextView dialogButton = (TextView) dialog.findViewById(R.id.idalertok);

                                final String enteredcode = text.getText().toString();
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        final String enteredcode = text.getText().toString();
                                        noteRef = db.collection("orders").document(documentSnapshot.get("order_id").toString());
                                        noteRef.get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        if (documentSnapshot.exists()) {
                                                            String pickup_code = documentSnapshot.getString("pickup_code");

                                                            if (enteredcode.equals(pickup_code)) {

                                                                dialog.dismiss();
                                                                startActivity(new Intent(getActivity(), ShopMainActivity.class));
                                                                getActivity().finish();

                                                                DocumentReference orderRefAccept = db.collection("orders").document(documentSnapshot.get("order_id").toString());
                                                                orderRefAccept.update("status", "3");

                                                                Toast.makeText(getActivity(), "Order Picked up succesfully!", Toast.LENGTH_SHORT).show();

                                                            } else {
                                                                textViewIncoorectCode.setVisibility(View.VISIBLE);
                                                                text.setText("");
                                                            }

                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });

                                    }
                                });

                                dialog.show();
//                                order_status = "1";
//                                DocumentReference orderRefAccept = db.collection("orders").document(id);
//                                orderRefAccept.update(KEY_ORDER_STATUS, order_status);
                            }
                        });
                builder.show();


            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterRejected.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterRejected.stopListening();
    }
}


