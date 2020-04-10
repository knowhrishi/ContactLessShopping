package com.example.contactlessshopping.Customers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
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

import com.airbnb.lottie.LottieAnimationView;
import com.example.contactlessshopping.R;
import com.google.android.gms.location.FusedLocationProviderClient;
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

    LottieAnimationView lottieAnimationView;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    EditText email, password;
    Button submit, signup;
    private FirebaseAuth mAuth;
    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    String LAT, LON;
    double dlat, dlon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_customer);


        lottieAnimationView=findViewById(R.id.loading);

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    Login_customer.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        }

        email = (EditText) findViewById(R.id.login_email);
        password = (EditText) findViewById(R.id.login_pass);
        submit = (Button) findViewById(R.id.submit_login);
        mAuth = FirebaseAuth.getInstance();
//        if (mAuth.getCurrentUser() != null) {
//            startActivity(new Intent(Login_customer.this, Customer_MainActivity.class));
//            finish();
//        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //getLastLocation();

        //Toast.makeText(this, LAT + "\n" + LON, Toast.LENGTH_LONG).show();

        signup = (Button) findViewById(R.id.signupbt);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("in signup", "successfull");

                Intent intent = new Intent(Login_customer.this, Customer_registration.class);
                startActivity(intent);


            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getLastLocation();

                String semail = email.getText().toString();
                String spass = password.getText().toString();
                lottieAnimationView.setVisibility(View.VISIBLE);
                lottieAnimationView.playAnimation();


                if (!TextUtils.isEmpty(semail) && !TextUtils.isEmpty(spass)) {
                    loginuser(semail, spass);
//                    lottieAnimationView.cancelAnimation();
//                    lottieAnimationView.setVisibility(View.INVISIBLE);
                } else {
//                    lottieAnimationView.cancelAnimation();
//                    lottieAnimationView.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login_customer.this, "Failed Login: Empty Inputs are not allowed", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void loginuser(String email, String pass) {
        getLastLocation();
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Intent intent=new Intent(Login_customer.this,Customers);
                            // startActivity(intent);
                            Intent intent = new Intent(Login_customer.this, customer_dash.class);
                            intent.putExtra("intendLatitude", LAT);
                            intent.putExtra("intentLongitude", LON);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Toast.makeText(Login_customer.this, "Successfull Login", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            lottieAnimationView.cancelAnimation();
                            lottieAnimationView.setVisibility(View.INVISIBLE);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Login_customer.this, "Auth Failed!", Toast.LENGTH_SHORT).show();
                Log.d("tag", e.toString());
                password.getText().clear();
                lottieAnimationView.cancelAnimation();
                lottieAnimationView.setVisibility(View.INVISIBLE);
            }
        });

    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    dlat = location.getLatitude();
                                    LAT = String.valueOf(dlat);
                                    dlon = location.getLongitude();
                                    LON = String.valueOf(dlon);
                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            dlat = mLastLocation.getLatitude();
            LAT = String.valueOf(dlat);
            dlon = mLastLocation.getLongitude();
            LON = String.valueOf(dlon);
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }
}
