package com.example.contactlessshopping.Customers;

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

public class ShopsAdapter extends FirestoreRecyclerAdapter<Shopsclass , ShopsAdapter.NoteHolder> {
    private OrderAdapterPending.OnItemClickListener listener;
    /**
     *
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ShopsAdapter(@NonNull FirestoreRecyclerOptions<Shopsclass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int i, @NonNull Shopsclass model) {

        holder.textName.setText(model.getshop_name());
        holder.textTitle.setText(model.getfrom_time());
        holder.textCompany.setText(model.getto_time());

    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_shops, parent, false);

        return new NoteHolder(v);
    }

     class NoteHolder extends RecyclerView.ViewHolder {
        private View view;


        TextView textName,textTitle,textCompany;

        public NoteHolder(View itemView) {
            super(itemView);
            view=itemView;


            textName=(TextView)view.findViewById(R.id.name_shop);

            textTitle=(TextView)view.findViewById(R.id.from);

            textCompany=(TextView)view.findViewById(R.id.to);

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

    public void setOnItemClickListener(OrderAdapterPending.OnItemClickListener listener) {
        this.listener = listener;
    }

}
