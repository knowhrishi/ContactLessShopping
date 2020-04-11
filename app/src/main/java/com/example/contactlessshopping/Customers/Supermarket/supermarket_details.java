package com.example.contactlessshopping.Customers.Supermarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactlessshopping.Customers.ShopDetails;
import com.example.contactlessshopping.Customers.Upload_list;
import com.example.contactlessshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Value;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.example.contactlessshopping.Shops.Main.OrderDetails.KEY_ORDER_STATUS;

public class supermarket_details extends AppCompatActivity {
    private FirebaseAuth auth;
    private DocumentReference noteRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("tokens");

//    ImageView imageView;
//    Button button;
//    EditText editText;
//    String EditTextValue;
//    Thread thread;
//    public final static int QRcodeWidth = 500;
//    Bitmap bitmap;


    String shop_id, shop_name, capacity, token_no;

    Button get_token;
    TextView token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket_details);
        auth = FirebaseAuth.getInstance();

        final Intent intent = getIntent();
        shop_id = intent.getStringExtra("shop_id");
        shop_name = intent.getStringExtra("shop_name");

        //imageView = (ImageView) findViewById(R.id.imageView);

        get_token = findViewById(R.id.get_token);
        token = findViewById(R.id.token_no);
        get_token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("token_slots").document(shop_id).get()
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

                                        SecureRandom random = new SecureRandom();
                                        int num = random.nextInt(100000);
                                        token_no = String.format("%05d", num);

//                                        try {
//                                            bitmap = TextToImageEncode(token_no);
//
//                                            imageView.setImageBitmap(bitmap);
//
//                                        } catch (WriterException e) {
//                                            e.printStackTrace();
//                                        }

                                        List<String> keys = new ArrayList<String>(data.keySet());
                                        Collections.sort(keys);

                                        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                                        Log.d("time", currentTime);
                                        Log.d("Tag", " keyset sorted : " + keys);
                                        for (String i : keys) {


                                            List<String> mylist = (List<String>) data.get(i);


                                            String[] s = i.split("-");
                                            Date slot_from = new Date();
                                            Date slot_to = new Date();
                                            Date currtime = new Date();

                                            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                                            try {
                                                slot_from = dateFormat.parse(s[0].trim());
                                                slot_to = dateFormat.parse(s[1].trim());
                                                currtime = dateFormat.parse(currentTime.trim());
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }


                                            if (slot_from.before(currtime) && currtime.before(slot_to)) {
                                                if (mylist.size() != 10) {

                                                    DocumentReference orderRefAccept = db.collection("token_slots").document(shop_id);
                                                    orderRefAccept.update(i, FieldValue.arrayUnion(auth.getUid()));
                                                    Toast.makeText(supermarket_details.this, i + " slot is allocated to you!!", Toast.LENGTH_SHORT).show();

                                                    Toast.makeText(supermarket_details.this, auth.getUid().toString(), Toast.LENGTH_SHORT).show();

                                                    Map<String,Object> token_doc=new HashMap<>();
                                                    token_doc.put("shop_id",shop_id);
                                                    token_doc.put("token_no",token_no);
                                                    token_doc.put("customer_id",auth.getUid());
                                                    token_doc.put("slot_allocated",i);

                                                    db.collection("tokens").document(auth.getUid().toString()).set(token_doc);
                                                    Toast.makeText(supermarket_details.this, token_no, Toast.LENGTH_SHORT).show();
                                                    token.setText(token_no);
                                                    break;

                                                }
                                            } else if (slot_from.after(currtime)) {
                                                if (mylist.size() != 10) {

                                                    DocumentReference orderRefAccept = db.collection("tokens").document(shop_id);
                                                    orderRefAccept.update(i, FieldValue.arrayUnion(token_no));
                                                    Toast.makeText(supermarket_details.this, i + " slot is allocated to you!!", Toast.LENGTH_SHORT).show();
                                                    Map<String, Object> map = new HashMap<>();
                                                    map.put("token_no", token_no);
                                                    Toast.makeText(supermarket_details.this, auth.getUid().toString(), Toast.LENGTH_SHORT).show();
                                                    db.collection("customers").document(auth.getUid().toString()).update("token_supermarket", token_no);
                                                    Toast.makeText(supermarket_details.this, token_no, Toast.LENGTH_SHORT).show();
                                                    token.setText(token_no);
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

            }
        });

    }

//    Bitmap TextToImageEncode (String Value) throws WriterException {
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