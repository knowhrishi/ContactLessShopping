package com.example.contactlessshopping.Customers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.Main.OrderAdapterPending;
import com.example.contactlessshopping.Shops.Main.OrderModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

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

    @Override
    protected void onBindViewHolder(@NonNull OrdersAdapter.NoteHolder holder, int i, @NonNull OrderModel model) {

        holder.textViewContent.setText(model.getOrder_no());

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
        TextView textViewContent;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            view=itemView;

            textViewContent = (TextView) itemView.findViewById(R.id.orderstext);


        }
    }

    public class OnItemClickListener {
        public void onItemClick(DocumentSnapshot snapshot, int position) {
        }
    }
}
