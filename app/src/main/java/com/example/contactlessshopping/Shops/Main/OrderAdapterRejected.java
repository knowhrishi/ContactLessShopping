package com.example.contactlessshopping.Shops.Main;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlessshopping.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class OrderAdapterRejected extends FirestoreRecyclerAdapter<OrderModel, OrderAdapterRejected.NoteHolder> {
    private OnItemClickListener listener;
    //Note upload;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public OrderAdapterRejected(@NonNull FirestoreRecyclerOptions<OrderModel> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull OrderModel model) {
        String fetched_status = model.getStatus();
        holder.textViewContent.setText(model.getCustomer_name());
        holder.textViewOrderNo.setText(model.getOrder_no());
        holder.textViewAllocatedSlot.setText(model.getPickup_slot());
//        if(fetched_status.equals("2")){
//            holder.textViewStatus.setText("Slot not allocated");
//            holder.textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
//        }

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_layout_rejected, parent, false);

//        context = parent.getContext();
        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        TextView textViewContent, textViewStatus, textViewOrderNo, textViewAllocatedSlot;

        public NoteHolder(@NonNull final View itemView) {
            super(itemView);

            textViewContent = (TextView) itemView.findViewById(R.id.idCustomerName);
            textViewStatus = (TextView) itemView.findViewById(R.id.orderStatus);
            textViewOrderNo = (TextView) itemView.findViewById(R.id.orderno);
            textViewAllocatedSlot = (TextView) itemView.findViewById(R.id.allocated_slot);


//
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
