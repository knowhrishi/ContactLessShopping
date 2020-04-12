package com.example.contactlessshopping.Shops;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.contactlessshopping.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class shop_edit_profile extends AppCompatActivity {

    private static final int Pick_Photo = 1;
    DatabaseReference databaseReference;
    Uri image;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_edit_profile);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Image");
        storageReference = FirebaseStorage.getInstance().getReference().child("Image_File");

    }


    public void UploadImage(View view) {


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Pick_Photo);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Pick_Photo && resultCode == RESULT_OK) {

            final Uri uri = data.getData();
            String userID= firebaseAuth.getCurrentUser().getUid();
            //this is for image file name
            final StorageReference filepath = storageReference.child("Profiles").child(userID);
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("link", String.valueOf(uri));
                            databaseReference.setValue(hashMap);
                            Toast.makeText(shop_edit_profile.this, "Done"+uri, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}
