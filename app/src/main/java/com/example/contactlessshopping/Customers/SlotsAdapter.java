package com.example.contactlessshopping.Customers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.Main.OrderAdapterPending;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class SlotsAdapter extends FirestoreRecyclerAdapter<SlotsModel, SlotsAdapter.NoteHolder> {
    private OnItemClickListener listener;

    Customer_MainActivity context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SlotsAdapter(@NonNull FirestoreRecyclerOptions<SlotsModel> options) {
        super(options);
    }

//    public SlotsAdapter(FirestoreRecyclerOptions<SlotsModel> options, Context context) {
//
//        super(options);
//
//        this.context = (Customer_MainActivity) context;
//    }


    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int i, @NonNull SlotsModel model) {

        holder.textSlots.setText(model.getSlot());


    }


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slots_layout, parent, false);

        return new NoteHolder(v);
    }



    class NoteHolder extends RecyclerView.ViewHolder {
        private View view;


        TextView textSlots;

        public NoteHolder(View itemView) {
            super(itemView);
            view = itemView;
            textSlots = (TextView) view.findViewById(R.id.idSlots);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });


        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(SlotsAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

}
