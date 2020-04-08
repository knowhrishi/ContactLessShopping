package com.example.contactlessshopping.Customers;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactlessshopping.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Upload_list extends AppCompatActivity {
    // views for button
    private Button btnUpload,btnSelect;
    ProgressDialog progressDialog;
    StorageReference Ref;



    String cust_name,shop_id,order_id,order_no,format;

    FirebaseAuth auth;
    DocumentReference docref;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference orderref = db.collection("orders");

    // view for image view
    private ImageView imageView;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;


    // instance for firebase storage and StorageReference


    List<String> product= Collections.<String>emptyList();;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadlist);
        Ref= FirebaseStorage.getInstance().getReference("orders");



        auth = FirebaseAuth.getInstance();
        final Intent intent = getIntent();
        shop_id=intent.getStringExtra("shop_id");

        SecureRandom random = new SecureRandom();
        int num = random.nextInt(100000);
        order_no= String.format("%05d", num);



        // initialise views
        btnSelect = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.image);

        docref = db.collection("customers").document(auth.getUid());
        docref.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            cust_name = documentSnapshot.getString("Name");
                            Toast.makeText(Upload_list.this,cust_name,Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        format = simpleDateFormat.format(new Date());

        // get the Firebase  storage reference

        // on pressing btnSelect SelectImage() is called
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });

        // on pressing btnUpload uploadImage() is called
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Fileuploader();
            }
        });
    }

    // Select Image method
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {


            Map<String, Object> note = new HashMap<>();
            note.put("customer_name",cust_name );
            note.put("shop_id",shop_id);
            note.put("status","0");
            note.put("timestamp",format);
            note.put("url",filePath);
            note.put("order_no",order_no);
            note.put("product",product);

            db.collection("orders").add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d("iiii iiiii iiiiiii ii", "DocumentSnapshot written with ID: " + documentReference.getId());
                    order_id=documentReference.getId();
                    Toast.makeText(Upload_list.this,order_id,Toast.LENGTH_SHORT).show();
                    Map<String, Object> data = new HashMap<>();
                    data.put("order_id", order_id);

                    db.collection("orders").document(order_id).set(data, SetOptions.merge());


                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("", "Error adding document", e);
                        }
                    });



        }
    }


    private String getExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void Fileuploader(){
        Ref= Ref.child(System.currentTimeMillis()+"."+getExtension(filePath));
        Ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //dismissing the progress dialog


                        Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

//
                                CollectionReference notebookRef = db
                                        .collection("orders");

                                Map<String, Object> note = new HashMap<>();
                                note.put("customer_name",cust_name );
                                note.put("shop_id",shop_id);
                                note.put("status","0");
                                note.put("timestamp",format);
                                note.put("url",uri.toString());
                                note.put("order_no",order_no);
                                note.put("product",product);


                                String noticeID =  UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
                                notebookRef.add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("iiii iiiii iiiiiii ii", "DocumentSnapshot written with ID: " + documentReference.getId());
                                        order_id = documentReference.getId();
                                        Toast.makeText(Upload_list.this, order_id, Toast.LENGTH_SHORT).show();
                                        Map<String, Object> data = new HashMap<>();
                                        data.put("order_id", order_id);

                                        db.collection("orders").document(order_id).set(data, SetOptions.merge());


                                    }
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("", "Error adding document", e);
                                            }
                                        });
                                finish();



                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
//                            progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //displaying the upload progress

                    }
                });

    }



}
