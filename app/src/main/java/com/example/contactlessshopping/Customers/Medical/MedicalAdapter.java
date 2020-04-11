package com.example.contactlessshopping.Customers.Medical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlessshopping.Customers.Customer_MainActivity;
import com.example.contactlessshopping.Customers.Shopsclass;
import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.Main.OrderAdapterPending;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class MedicalAdapter extends FirestoreRecyclerAdapter<Shopsclass, com.example.contactlessshopping.Customers.Medical.MedicalAdapter.NoteHolder> {
    private OrderAdapterPending.OnItemClickListener listener;
    double dlat, dlon;
    Medical_MainActivity context;
    /**
     *
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
//    public ShopsAdapter(@NonNull FirestoreRecyclerOptions<Shopsclass> options) {
//
//    }

    public MedicalAdapter(FirestoreRecyclerOptions<Shopsclass> options, double dlat, double dlon, Context context) {

        super(options);
        this.dlat = dlat;
        this.dlon = dlon;
        this.context = (Medical_MainActivity) context;
    }


    @Override
    protected void onBindViewHolder(@NonNull com.example.contactlessshopping.Customers.Medical.MedicalAdapter.NoteHolder holder, int i, @NonNull Shopsclass model) {
        String shop_lat="0", shop_lon="0";
        double lat1, lon1, lat2, lon2;

        holder.textName.setText(model.getshop_name());
        holder.textTitle.setText(model.getfrom_time());
        holder.textCompany.setText(model.getto_time());

        shop_lat = model.getLatitude();
        shop_lon = model.getLongitude();

        //Toast.makeText(context, model.getshop_name() + ": " + shop_lat + "\n" + shop_lon, Toast.LENGTH_SHORT).show();

        lat1 = Double.parseDouble(shop_lat);
        lon1 = Double.parseDouble(shop_lon);
        lat2 = dlat;
        lon2 = dlon;
        double dist = distance(lat1, lon1, lat2, lon2);
        double roundOffDist = Math.round(dist*100)/100;
        holder.textViewDistance.setText(String.format(String.valueOf(roundOffDist) + " KMs"));
    }


    @NonNull
    @Override
    public com.example.contactlessshopping.Customers.Medical.MedicalAdapter.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_shops, parent, false);

        return new com.example.contactlessshopping.Customers.Medical.MedicalAdapter.NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private View view;


        TextView textName,textTitle,textCompany,textViewDistance;

        public NoteHolder(View itemView) {
            super(itemView);
            view=itemView;


            textName=(TextView)view.findViewById(R.id.name_shop);

            textTitle=(TextView)view.findViewById(R.id.from);

            textCompany=(TextView)view.findViewById(R.id.to);
            textViewDistance = (TextView) view.findViewById(R.id.idDistance);

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
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;

            return (dist);
        }
    }


}

