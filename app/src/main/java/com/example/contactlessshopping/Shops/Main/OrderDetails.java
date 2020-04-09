package com.example.contactlessshopping.Shops.Main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.contactlessshopping.Customers.Make_list;
import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.ShopMainActivity;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class OrderDetails extends AppCompatActivity {

    TextView textViewCustName, textViewOrderNo, textViewStatus, textViewTime;
    RelativeLayout relativeLayoutiProductList;
    CardView cardViewCVProductImage;
    public RelativeLayout relativeLayout_relative_layout_progress;
    Button buttonAccept, buttonReject;
    ListView listview;
    ArrayList arrayList;
    List lstr;
    LottieAnimationView lottieAnimationView;
    ImageView img;
    URL url;
    String u;
    private ArrayAdapter<String> adapter;


    public static final String KEY_ORDER_STATUS = "status";
    private static final String TAG = "OrderDetails";
    String order_status;

    private FirebaseAuth auth;
    private DocumentReference noteRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("orders");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        textViewCustName = (TextView) findViewById(R.id.idCustomerName);
        textViewOrderNo = (TextView) findViewById(R.id.idOrderNo);
        textViewStatus = (TextView) findViewById(R.id.idOrderStatus);
        textViewTime = (TextView) findViewById(R.id.idOrderTimeStamp);
        buttonAccept = (Button) findViewById(R.id.idBtnAccept);
        buttonReject = (Button) findViewById(R.id.idBtnReject);
        listview = findViewById(R.id.list_order);
        img = (ImageView) findViewById(R.id.img);
        relativeLayoutiProductList = (RelativeLayout) findViewById(R.id.idProductListRV);
        cardViewCVProductImage = (CardView) findViewById(R.id.idCVProductImage);
        lottieAnimationView = (LottieAnimationView) findViewById(R.id.upload);
        relativeLayout_relative_layout_progress = (RelativeLayout) findViewById(R.id.relative_layout_progress);

        arrayList = new ArrayList<String>();
        //arrayList.add("first");


        auth = FirebaseAuth.getInstance();


        final Intent intent = getIntent();
        textViewCustName.setText(intent.getStringExtra("intentCustomerName").toUpperCase());
        textViewOrderNo.setText(intent.getStringExtra("intentOrderNo").toUpperCase());
        textViewStatus.setText("PENDING");
        textViewTime.setText(intent.getStringExtra("intentOrderTimeStamp").toUpperCase());


        final String id = intent.getStringExtra("intentOrderId");


        DocumentReference orderref = db.collection("orders").document(id);

        orderref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    List<String> mylist = (List<String>) documentSnapshot.get("product");

                    //Toast.makeText(OrderDetails.this,mylist.toString(),Toast.LENGTH_SHORT).show();
                    if (mylist.size() != 0) {

                        relativeLayoutiProductList.setVisibility(View.VISIBLE);
                        cardViewCVProductImage.setVisibility(View.GONE);
                        arrayList = new ArrayList<String>(mylist);
                        // Toast.makeText(OrderDetails.this, arrayList.toString() + "arraylist", Toast.LENGTH_SHORT).show();

                        adapter = new ArrayAdapter<String>(OrderDetails.this, android.R.layout.simple_list_item_1, arrayList);
                        listview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        //img.setVisibility(View.INVISIBLE);
                    } else {

                        relativeLayoutiProductList.setVisibility(View.GONE);
                        cardViewCVProductImage.setVisibility(View.VISIBLE);
                        listview.setVisibility(View.INVISIBLE);



                        lottieAnimationView.setVisibility(View.VISIBLE);
                        relativeLayout_relative_layout_progress.setVisibility(View.VISIBLE);
                        lottieAnimationView.playAnimation();


                        u = documentSnapshot.get("url").toString();
                        Picasso.get().load(u).into(img, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                if (lottieAnimationView != null) {
                                    lottieAnimationView.setVisibility(View.GONE);
                                    relativeLayout_relative_layout_progress.setVisibility(View.GONE);
                                    lottieAnimationView.pauseAnimation();
                                }
                            }
                            @Override
                            public void onError(Exception e) {
                            }
                        });
                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code to show image in full screen:
                new PhotoFullPopupWindow(OrderDetails.this, R.layout.popup_photo_full, view, u, null);

            }
        });

        buttonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(
                        OrderDetails.this);
                builder.setTitle("Are you sure you want to accept order?");
                builder.setMessage("Once order accepted you cannot cancel the order.");
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface alertDialog,
                                                int which) {
                                final Dialog dialog = new Dialog(OrderDetails.this);
                                dialog.setContentView(R.layout.order_accept_dialog);
                                TextView text = (TextView) dialog.findViewById(R.id.orderconfirmTV);
                                text.setText("Start preparing the order for " + intent.getStringExtra("intentCustomerName").toUpperCase() + "\nand Let them know once order is prepared in accepted section.\nThankyou.");
                                Button dialogButton = (Button) dialog.findViewById(R.id.idalertok);
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        startActivity(new Intent(OrderDetails.this, ShopMainActivity.class));
                                        finish();
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


                                order_status = "1";
                                DocumentReference orderRefAccept = db.collection("orders").document(id);
                                orderRefAccept.update(KEY_ORDER_STATUS, order_status);
                            }
                        });
                builder.show();
            }
        });


        buttonReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order_status = "2";
                DocumentReference orderRefRej = db.collection("orders").document(id);
                orderRefRej.update(KEY_ORDER_STATUS, order_status);
                startActivity(new Intent(OrderDetails.this, ShopMainActivity.class));
                finish();
            }
        });


    }


    public static class PhotoFullPopupWindow extends PopupWindow {

        View view;
        Context mContext;
        PhotoView photoView;
        ProgressBar loading;
        ViewGroup parent;
        private static PhotoFullPopupWindow instance = null;


        public PhotoFullPopupWindow(Context ctx, int layout, View v, String imageUrl, Bitmap bitmap) {
            super(((LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.popup_photo_full, null), ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            if (Build.VERSION.SDK_INT >= 21) {
                setElevation(5.0f);
            }
            this.mContext = ctx;
            this.view = getContentView();
            ImageButton closeButton = (ImageButton) this.view.findViewById(R.id.ib_close);
            setOutsideTouchable(true);

            setFocusable(true);
            // Set a click listener for the popup window close button
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Dismiss the popup window
                    dismiss();
                }
            });
            //---------Begin customising this popup--------------------

            photoView = (PhotoView) view.findViewById(R.id.image);
            loading = (ProgressBar) view.findViewById(R.id.loading);
            photoView.setMaximumScale(6);
            parent = (ViewGroup) photoView.getParent();
            // ImageUtils.setZoomable(imageView);
            //----------------------------
            if (bitmap != null) {
                loading.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= 16) {
                    parent.setBackground(new BitmapDrawable(mContext.getResources(), Constants.fastblur(Bitmap.createScaledBitmap(bitmap, 50, 50, true))));// ));
                } else {
                    onPalette(Palette.from(bitmap).generate());

                }
                photoView.setImageBitmap(bitmap);
            } else {
                loading.setIndeterminate(true);
                loading.setVisibility(View.VISIBLE);
                Glide.with(ctx).asBitmap()
                        .load(imageUrl)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                loading.setIndeterminate(false);
                                loading.setBackgroundColor(Color.LTGRAY);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                if (Build.VERSION.SDK_INT >= 16) {
                                    parent.setBackground(new BitmapDrawable(mContext.getResources(), Constants.fastblur(Bitmap.createScaledBitmap(resource, 50, 50, true))));// ));
                                } else {
                                    onPalette(Palette.from(resource).generate());

                                }
                                photoView.setImageBitmap(resource);

                                loading.setVisibility(View.GONE);
                                return false;
                            }
                        })


                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(photoView);

                showAtLocation(v, Gravity.CENTER, 0, 0);
            }
            //------------------------------

        }

        public void onPalette(Palette palette) {
            if (null != palette) {
                ViewGroup parent = (ViewGroup) photoView.getParent().getParent();
                parent.setBackgroundColor(palette.getDarkVibrantColor(Color.GRAY));
            }
        }

    }
}
