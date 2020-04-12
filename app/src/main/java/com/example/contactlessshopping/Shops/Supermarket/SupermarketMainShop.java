package com.example.contactlessshopping.Shops.Supermarket;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactlessshopping.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SupermarketMainShop extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth;
    private DocumentReference noteRef;

    private TextView textViewShopname;
    private TextView textViewTiming;

    Button buttonVerifyToken;

//    ImageView imageView;
//    Button button;
//    public final static int QRcodeWidth = 500;
//    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket_main_shop);

//        imageView = (ImageView) findViewById(R.id.imageView);
        auth = FirebaseAuth.getInstance();
        textViewShopname = (TextView) findViewById(R.id.idName);
        textViewTiming = (TextView) findViewById(R.id.isShopTiming);
        buttonVerifyToken = (Button) findViewById(R.id.idVerifyToken);

        buttonVerifyToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(SupermarketMainShop.this);
                dialog.setContentView(R.layout.order_pickup_shop_dialog);
                //final TextView textViewIncoorectCode = (TextView) dialog.findViewById(R.id.incorrectcode);
                final EditText text = (EditText) dialog.findViewById(R.id.tokenentered);
                TextView dialogButton = (TextView) dialog.findViewById(R.id.idalertok);
                final String enteredcode = text.getText().toString();




                    dialog.show();
                }
            });


            showInfo();
        }


        public void showInfo () {


            noteRef = db.collection("shops").document(auth.getUid());
            noteRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String shop_name = documentSnapshot.getString("shop_name");
                                String shop_from_time = documentSnapshot.getString("from_time");
                                String shop_to_time = documentSnapshot.getString("to_time");

                                textViewShopname.setText(shop_name);
                                textViewTiming.setText(shop_from_time + "-" + shop_to_time);

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }

//    Bitmap TextToImageEncode(String Value) throws WriterException {
//        BitMatrix bitMatrix;
//        try {
//            bitMatrix = new MultiFormatWriter().encode(
//                    Value,
//                    BarcodeFormat.DATA_MATRIX.QR_CODE,
//                    QRcodeWidth, QRcodeWidth, null
//            );
//
//        } catch (IllegalArgumentException Illegalargumentexception) {
//
//            return null;
//        }
//        int bitMatrixWidth = bitMatrix.getWidth();
//
//        int bitMatrixHeight = bitMatrix.getHeight();
//
//        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];
//
//        for (int y = 0; y < bitMatrixHeight; y++) {
//            int offset = y * bitMatrixWidth;
//
//            for (int x = 0; x < bitMatrixWidth; x++) {
//
//                pixels[offset + x] = bitMatrix.get(x, y) ?
//                        getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
//            }
//        }
//        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
//
//        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
//        return bitmap;
//    }
    }
