package com.example.contactlessshopping.Shops.Main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.contactlessshopping.Customers.Supermarket.supermarket_details;
import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.ShopMainActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class FragmentAccepted extends Fragment {
    View view;


    private RecyclerView recyclerView;
    FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("orders");
    String order_status;
    private OrderAdapterAccepted adapterAccepted;

    public FragmentAccepted(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_accepted_request, container, false);
        auth = FirebaseAuth.getInstance();

        setupRecyclerView();
        return view;
    }
    private void setupRecyclerView() {
        Query query = db.collection("orders").whereEqualTo("shop_id",auth.getUid())
                .whereEqualTo("status","1")
                .orderBy("status", Query.Direction.ASCENDING);


        FirestoreRecyclerOptions<OrderModel> options = new FirestoreRecyclerOptions.Builder<OrderModel>()
                .setQuery(query, OrderModel.class)
                .build();

        adapterAccepted = new OrderAdapterAccepted(options, getActivity());

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterAccepted.notifyDataSetChanged();
        recyclerView.setAdapter(adapterAccepted);


        adapterAccepted.setOnItemClickListener(new OrderAdapterAccepted.OnItemClickListener() {
            @Override
            public void onItemClick(final DocumentSnapshot documentSnapshot, int position) {



                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                builder.setTitle("Slot Allocation");
                builder.setMessage("By Accepting user will get next available slot to pick order.");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface alertDialog,
                                                int which) {
                                final Dialog dialog = new Dialog(getActivity());

                                dialog.setContentView(R.layout.order_slot_allocate_dialog);

                                final TextView text = (TextView) dialog.findViewById(R.id.slotconfirmV);

                                LottieAnimationView lottieAnimationView;
                                lottieAnimationView = (LottieAnimationView) dialog.findViewById(R.id.timer);
                                lottieAnimationView.setVisibility(View.VISIBLE);



                                /**************************************************************/


                                db.collection("order_slot").document(auth.getUid()).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        Log.d("Tag", "DocumentSnapshot data: " + document.getData());
                                                        Map<String, Object> data = document.getData();
                                                        data.remove("date");
                                                        data.remove("shop_id");
                                                        Log.d("Tag", "DocumentSnapshot keyset: " + data.keySet());
                                                        Log.d("Tag", "DocumentSnapshot entryset: " + data.entrySet());

//                                                        SecureRandom random = new SecureRandom();
//                                                        int num = random.nextInt(100000);
//                                                        token_no = String.format("%05d", num);

                                                        List<String> keys = new ArrayList<String>(data.keySet());
                                                        Collections.sort(keys);

                                                        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                                                        Log.d("time", currentTime);
                                                        Log.d("Tag", " keyset sorted : " + keys);
                                                        for (String i : keys) {

                                                            List<String> mylist =(List<String>)data.get(i);


                                                            String[] s = i.split("-");
                                                            Date slot_from=new Date();
                                                            Date slot_to=new Date();
                                                            Date currtime=new Date();

                                                            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                                                            try {
                                                                slot_from = dateFormat.parse(s[0].trim());
                                                                slot_to=dateFormat.parse(s[1].trim());
                                                                currtime=dateFormat.parse(currentTime.trim());
                                                            } catch (ParseException e) {
                                                                e.printStackTrace();
                                                            }



                                                            if(slot_from.before(currtime) && currtime.before(slot_to) )
                                                            {
                                                                if (mylist.size() != 10) {

                                                                    DocumentReference orderRefAccept = db.collection("order_slot").document(auth.getUid());
                                                                    orderRefAccept.update(i, FieldValue.arrayUnion(documentSnapshot.get("order_no").toString()));
                                                                    Toast.makeText(getActivity(), i + " slot is allocated to you!!", Toast.LENGTH_SHORT).show();

                                                                    Toast.makeText(getActivity(), auth.getUid().toString(), Toast.LENGTH_SHORT).show();
                                                                    db.collection("orders").document(documentSnapshot.get("order_id").toString()).update("pickup_slot", i);
                                                                    //Toast.makeText(getActivity(), token_no, Toast.LENGTH_SHORT).show();
                                                                    text.setText("Slot " + i + " has been allocated to order " + documentSnapshot.get("order_no").toString() + ".");
                                                                    break;

                                                                }
                                                            }
                                                            else if(slot_from.after(currtime))
                                                            {
                                                                if (mylist.size() != 10) {

                                                                    DocumentReference orderRefAccept = db.collection("order_slot").document(auth.getUid());
                                                                    orderRefAccept.update(i, FieldValue.arrayUnion(documentSnapshot.get("order_no").toString()));
                                                                    Toast.makeText(getActivity(), i + " slot is allocated to you!!", Toast.LENGTH_SHORT).show();

                                                                    Toast.makeText(getActivity(), auth.getUid().toString(), Toast.LENGTH_SHORT).show();
                                                                    db.collection("orders").document(documentSnapshot.get("order_id").toString()).update("pickup_slot", i);
                                                                    //Toast.makeText(getActivity(), token_no, Toast.LENGTH_SHORT).show();
                                                                    text.setText("Slot " + i + " has been allocated to order " + documentSnapshot.get("order_no").toString() + ".");
                                                                    break;

                                                                }

                                                            }

                                                        }

                                                    } else {
                                                        Log.d("Tag", "No such document");
                                                    }
                                                } else {
                                                    Log.d("Tag", "get failed with ", task.getException());
                                                }
                                            }
                                        });









                                /*****************************************************************/





                                text.setText("");

                                Button dialogButton = (Button) dialog.findViewById(R.id.idalertok);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        startActivity(new Intent(getActivity(), ShopMainActivity.class));
                                       getActivity().finish();
                                    }
                                });
                                dialog.show();
                                final Timer t = new Timer();
                                t.schedule(new TimerTask() {
                                    public void run() {
                                        dialog.dismiss(); // when the task active then close the dialog
                                        t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
//                                        startActivity(new Intent(OrderDetails.this, ShopMainActivity.class));
//                                        finish();
                                    }
                                }, 7000); // after 2 second (or 2000 miliseconds), the task will be active.


                                order_status = "2";
                                DocumentReference orderRefAccept = db.collection("orders").document(documentSnapshot.get("order_id").toString());
                                orderRefAccept.update("status", order_status);
                            }
                        });
                builder.show();



            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        adapterAccepted.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterAccepted.stopListening();
    }
}

