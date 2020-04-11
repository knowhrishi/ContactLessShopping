package com.example.contactlessshopping.Customers;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.Main.OrderAdapterAccepted;
import com.example.contactlessshopping.Shops.Main.OrderAdapterPending;
import com.example.contactlessshopping.Shops.Main.OrderModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import static androidx.core.content.ContextCompat.getColor;
import static com.google.common.io.Resources.getResource;

public class OrdersAdapter extends FirestoreRecyclerAdapter<OrderModel, OrdersAdapter.NoteHolder> {

    private OrdersAdapter.OnItemClickListener listener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public OrdersAdapter(@NonNull FirestoreRecyclerOptions<OrderModel> options) {
        super(options);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull OrdersAdapter.NoteHolder holder, int i, @NonNull OrderModel model) {

        holder.textViewContent.setText(model.getOrder_no());
        holder.textViewShopName.setText(model.getShop_name());
        if(Integer.parseInt(model.getStatus())==1)
        {
            holder.textViewStatus.setText("Slot not Allocated");
            holder.textViewStatus.setTextColor(R.color.red);
        }
        else if(Integer.parseInt(model.getStatus())==2)
        {
            holder.textViewStatus.setText("Pick Up Pending");
            holder.textViewStatus.setTextColor(R.color.green);
        }
        else if(Integer.parseInt(model.getStatus())==3)
        {
            holder.textViewStatus.setText("Give Feedback");
            holder.textViewStatus.setTextColor(R.color.red);
        }


    }

    @NonNull
    @Override
    public OrdersAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_list, parent, false);

        return new OrdersAdapter.NoteHolder(v);
        
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        private View view;
        TextView textViewContent, textViewShopName,textViewStatus;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            view=itemView;

            textViewContent = (TextView) itemView.findViewById(R.id.orderstext);
            textViewShopName = (TextView) itemView.findViewById(R.id.shop_name);
            textViewStatus=itemView.findViewById(R.id.status);


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

    public void setOnItemClickListener(OrdersAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
