package com.example.contactlessshopping.Shops;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactlessshopping.MainActivity;
import com.example.contactlessshopping.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class shop_profile extends AppCompatActivity {

    TextView name,number,email,address,open,close,capacity;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    String userID;
    Button edit,changeProfileImage;
    FloatingActionButton fab;
    CircleImageView imageView;
    FirebaseFirestore fStore;
    StorageReference storageReference;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String KEY_PROFILE_URL = "profile_pic_url";

    private DocumentReference noteRef;
    FirebaseAuth fAuth;
    FirebaseUser user;
    String userId,profile_pic_url ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_profile);

        final int GALLERY_INTENT_CODE = 1023 ;
        fab=findViewById(R.id.edit_profile_picture);
        name=findViewById(R.id.shop_name);
        number=findViewById(R.id.shop_number);
        email=findViewById(R.id.shop_email);
        address=findViewById(R.id.shop_address);
        open=findViewById(R.id.shop_open);
        close=findViewById(R.id.shop_close);
        capacity=findViewById(R.id.shop_capacity);
        imageView =(CircleImageView) findViewById(R.id.imageview_account_profile);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .placeholder(R.drawable.store)
                        .resize(400,400)
                        .into(imageView);
            }
        });
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();



        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        userID= firebaseAuth.getCurrentUser().getUid();
        DocumentReference documentReference =firebaseFirestore.collection("shops").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("shop_name"));
                number.setText(documentSnapshot.getString("phone_number"));
                email.setText(documentSnapshot.getString("email_id"));
                address.setText(documentSnapshot.getString("address"));
                open.setText(documentSnapshot.getString("from_time"));
                close.setText(documentSnapshot.getString("to_time"));
                capacity.setText(documentSnapshot.getString("capacity"));
            }
        });

//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(shop_profile.this,shop_edit_profile.class);
//                startActivity(intent);
//            }
//        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
                uploadLink();

            }
        });


    }

    private void uploadLink() {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();

                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);


            }
        }

    }

    // uplaod image to firebase storage
    private void uploadImageToFirebase(final Uri imageUri) {

//        final StorageReference sRef = storageReference.child("shops"+firebaseAuth.getUid()+"link"+imageUri);
        final StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        noteRef = db.collection("shops").document(fAuth.getUid());
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageView);

                        final String profil_url = uri.toString();

                        Map<String, Object> newLinkData = new HashMap<>();
                        newLinkData.put(KEY_PROFILE_URL, profil_url);
                        db.collection("shops").document(fAuth.getUid()).update(newLinkData);
                        finish();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(shop_profile.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });



            }

}

