package com.example.contactlessshopping.Shops.Main;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlessshopping.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class OrderAdapterAccepted extends FirestoreRecyclerAdapter<OrderModel, OrderAdapterAccepted.NoteHolder> {
    private OnItemClickListener listener;
    //Note upload;
    Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public OrderAdapterAccepted(@NonNull FirestoreRecyclerOptions<OrderModel> options, Context context) {
        super(options);
        this.context = context;
    }


    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull OrderModel model) {

        String fetched_status = model.getStatus();
        holder.textViewContent.setText(model.getCustomer_name());
        holder.textViewOrderNo.setText(model.getOrder_no());
        if(fetched_status.equals("1")){
            holder.textViewStatus.setText("Slot not allocated");
            holder.textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
//        if(fetched_status.equals("2")){
//            holder.textViewStatus.setText("Invalid Order");
//            holder.textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
//        }
//        if(fetched_status.equals("3")){
//            holder.textViewStatus.setText("Slot Allocated");
//            holder.textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.pale_yellow));
//        }
//        if(fetched_status.equals("4")){
//            holder.textViewStatus.setText("Order successfully picked-up");
//            holder.textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
//        }

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_layout_accepted, parent, false);

//        context = parent.getContext();
        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        TextView textViewContent, textViewStatus, textViewOrderNo;

        public NoteHolder(@NonNull final View itemView) {
            super(itemView);

            textViewContent = (TextView) itemView.findViewById(R.id.idCustomerName);
            textViewStatus = (TextView) itemView.findViewById(R.id.orderStatus);
            textViewOrderNo = (TextView) itemView.findViewById(R.id.orderno);


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
