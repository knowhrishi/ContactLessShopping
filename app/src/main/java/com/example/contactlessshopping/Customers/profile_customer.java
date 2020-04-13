package com.example.contactlessshopping.Customers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactlessshopping.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class profile_customer extends AppCompatActivity {

    TextView name,number,email,Customer_no,open,close,capacity;
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
        setContentView(R.layout.activity_profile_customer);

        //nav
        BottomNavigationView bottomNavigationView= findViewById(R.id.nav);

        bottomNavigationView.setSelectedItemId(R.id.item3);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.item1:
                        startActivity(new Intent(getApplicationContext(), Customer_MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.item2:
                        startActivity(new Intent(getApplicationContext(), ManageOrders.class));
                        overridePendingTransition(0,0);
                        return true;

                        case R.id.item3:

                        return true;
                }
                return false;
            }
        });



        final int GALLERY_INTENT_CODE = 1023 ;
        fab=findViewById(R.id.edit_profile_picture);
        name=findViewById(R.id.shop_name);
        email=findViewById(R.id.shop_email);
        Customer_no=findViewById(R.id.shop_address);
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
        DocumentReference documentReference =firebaseFirestore.collection("customers").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("Name"));

//                email.setText(documentSnapshot.getString("emailid"));
                Customer_no.setText(documentSnapshot.getString("Customer_no"));

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
        noteRef = db.collection("customers").document(fAuth.getUid());
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
                        db.collection("customers").document(fAuth.getUid()).update(newLinkData);
                        finish();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile_customer.this, "Failed.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
