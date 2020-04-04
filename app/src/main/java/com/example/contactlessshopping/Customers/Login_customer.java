package com.example.contactlessshopping.Customers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.contactlessshopping.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class Login_customer extends AppCompatActivity {

    EditText email,password;
    Button submit,signup;
    private FirebaseAuth mAuth;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    public static double latitude;
    public static double longitude;
    public static String LATITUDE,LONGITUDE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_customer);
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    Login_customer.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        }
getCurrentLocation();
        //Toast.makeText(this, latitude + "\n"  +longitude, Toast.LENGTH_SHORT).show();

        email=(EditText)findViewById(R.id.login_email);
        password=(EditText)findViewById(R.id.login_pass);
        submit=(Button)findViewById(R.id.submit_login);
        mAuth = FirebaseAuth.getInstance();





        signup=(Button)findViewById(R.id.signupbt);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.d("in signup","successfull");

                Intent intent=new Intent(Login_customer.this,Customer_registration.class);
                startActivity(intent);


            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String semail=email.getText().toString();
                String spass=password.getText().toString();

                if (!TextUtils.isEmpty(semail) && !TextUtils.isEmpty(spass)) {
                    loginuser(semail, spass);
                } else {

                    Toast.makeText(Login_customer.this, "Failed Login: Empty Inputs are not allowed", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    private void loginuser(String email,String pass)
    {


        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           // Intent intent=new Intent(Login_customer.this,Customers);
                           // startActivity(intent);
                            Toast.makeText(Login_customer.this,"Successfull Login",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Login_customer.this,Customer_MainActivity.class);
                            startActivity(intent);


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Login_customer.this, "Auth Failed!", Toast.LENGTH_SHORT).show();
                Log.d("tag", e.toString());
                password.getText().clear();
            }
        });





    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation()  {


        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(Login_customer.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(Login_customer.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int lastestLocationIndex = locationResult.getLocations().size() - 1;
                             latitude =
                                    locationResult.getLocations().get(lastestLocationIndex).getLatitude();
                             longitude =
                                    locationResult.getLocations().get(lastestLocationIndex).getLongitude();

                           // Toast.makeText(Login_customer.this, "LAT - "+latitude + "\n" + "LON: " + longitude, Toast.LENGTH_SHORT).show();
//
                            LATITUDE = String.format(String.valueOf(latitude));
                            LONGITUDE = String.format(String.valueOf(longitude));
//                            LOC=String.format(Locale.US, "%s -- %s", LATITUDE, LONGITUDE);
                            Log.d("lati:", String.valueOf(LATITUDE));
                            Log.d("lon::", String.valueOf(LONGITUDE));

                            //email.setText();

                        }
                    }
                }, Looper.getMainLooper());

    }
}
