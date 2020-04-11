package com.example.contactlessshopping.Customers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.Main.ShopLogin;
import com.example.contactlessshopping.Shops.ShopMainActivity;
import com.example.contactlessshopping.Shops.ShopRegistration;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Login_customer extends AppCompatActivity {

//    LottieAnimationView lottieAnimationView;
//    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
//    EditText email, password;
//    Button submit, signup;
//    private FirebaseAuth mAuth;
//    int PERMISSION_ID = 44;
//    FusedLocationProviderClient mFusedLocationClient;
//    String LAT, LON;
//    double dlat, dlon;

    FirebaseAuth fAuth;
    String otpCode;
    String verificationId;
    EditText phone,optEnter;
    Button next;
    CountryCodePicker countryCodePicker;
    PhoneAuthCredential credential;
    Boolean verificationOnProgress = false;
    ProgressBar progressBar;
    TextView state,resend;
    PhoneAuthProvider.ForceResendingToken token;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_customer);

        phone = findViewById(R.id.phone);
        optEnter = findViewById(R.id.codeEnter);
        countryCodePicker = findViewById(R.id.ccp);
        next = findViewById(R.id.nextBtn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        state = findViewById(R.id.state);
        resend = findViewById(R.id.resendOtpBtn);

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo:: resend OTP
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!phone.getText().toString().isEmpty() && phone.getText().toString().length() == 10) {
                    if(!verificationOnProgress){
                        next.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        state.setVisibility(View.VISIBLE);
                        String phoneNum = "+"+countryCodePicker.getSelectedCountryCode()+phone.getText().toString();
                        Log.d("phone", "Phone No.: " + phoneNum);
                        requestPhoneAuth(phoneNum);
                    }else {
                        next.setEnabled(false);
                        optEnter.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        state.setText("Logging in");
                        state.setVisibility(View.VISIBLE);
                        otpCode = optEnter.getText().toString();
                        if(otpCode.isEmpty()){
                            optEnter.setError("Required");
                            return;
                        }

                        credential = PhoneAuthProvider.getCredential(verificationId,otpCode);
                        verifyAuth(credential);
                    }

                }else {
                    phone.setError("Valid Phone Required");
                }
            }
        });


    }

    private void requestPhoneAuth(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60L, TimeUnit.SECONDS,this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                        super.onCodeAutoRetrievalTimeOut(s);
                        Toast.makeText(Login_customer.this,
                                "OTP Timeout, Please Re-generate the OTP Again.", Toast.LENGTH_SHORT).show();
//                        resend.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        token = forceResendingToken;
                        verificationOnProgress = true;
                        progressBar.setVisibility(View.GONE);
                        state.setVisibility(View.GONE);
                        next.setText("Verify");
                        next.setEnabled(true);
                        optEnter.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                        // called if otp is automatically detected by the app
                        verifyAuth(phoneAuthCredential);

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(Login_customer.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void verifyAuth(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login_customer.this, "Phone Verified."+ Objects.requireNonNull(fAuth.getCurrentUser()).getUid(), Toast.LENGTH_SHORT).show();
                    checkUserProfile();
                }else {
                    progressBar.setVisibility(View.GONE);
                    state.setVisibility(View.GONE);
                    Toast.makeText(Login_customer.this, "Can not Verify phone and Create Account.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(fAuth.getCurrentUser() != null){
            progressBar.setVisibility(View.VISIBLE);
            state.setText("Checking..");
            state.setVisibility(View.VISIBLE);
            checkUserProfile();
        }
    }

    private void checkUserProfile() {
        DocumentReference docRef = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            docRef = fStore.collection("shops").document(Objects.requireNonNull(fAuth.getCurrentUser()).getUid());
        }
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    startActivity(new Intent(getApplicationContext(), Customer_MainActivity.class));
                    finish();
                }else {
                    //Toast.makeText(Register.this, "Profile Do not Exists.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Customer_registration.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login_customer.this, "Profile Do Not Exists", Toast.LENGTH_SHORT).show();
            }
        });

//        lottieAnimationView=findViewById(R.id.loading);
//
//        if (ContextCompat.checkSelfPermission(
//                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                    Login_customer.this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_CODE_LOCATION_PERMISSION
//            );
//        }
//
//        email = (EditText) findViewById(R.id.login_email);
//        password = (EditText) findViewById(R.id.login_pass);
//        submit = (Button) findViewById(R.id.submit_login);
//        mAuth = FirebaseAuth.getInstance();
////        if (mAuth.getCurrentUser() != null) {
////            startActivity(new Intent(Login_customer.this, Customer_MainActivity.class));
////            finish();
////        }
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        //getLastLocation();
//
//        //Toast.makeText(this, LAT + "\n" + LON, Toast.LENGTH_LONG).show();
//
//        signup = (Button) findViewById(R.id.signupbt);
//
//
//        signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Log.d("in signup", "successfull");
//
//                Intent intent = new Intent(Login_customer.this, Customer_registration.class);
//                startActivity(intent);
//
//
//            }
//        });
//
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                getLastLocation();
//
//                String semail = email.getText().toString();
//                String spass = password.getText().toString();
//                lottieAnimationView.setVisibility(View.VISIBLE);
//                lottieAnimationView.playAnimation();
//
//
//                if (!TextUtils.isEmpty(semail) && !TextUtils.isEmpty(spass)) {
//                    loginuser(semail, spass);
////                    lottieAnimationView.cancelAnimation();
////                    lottieAnimationView.setVisibility(View.INVISIBLE);
//                } else {
////                    lottieAnimationView.cancelAnimation();
////                    lottieAnimationView.setVisibility(View.INVISIBLE);
//                    Toast.makeText(Login_customer.this, "Failed Login: Empty Inputs are not allowed", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        });
//
//    }
//
//    private void loginuser(String email, String pass) {
//        getLastLocation();
//        lottieAnimationView.setVisibility(View.VISIBLE);
//        lottieAnimationView.playAnimation();
//
//        mAuth.signInWithEmailAndPassword(email, pass)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Intent intent=new Intent(Login_customer.this,Customers);
//                            // startActivity(intent);
//                            Intent intent = new Intent(Login_customer.this, customer_dash.class);
//                            intent.putExtra("intendLatitude", LAT);
//                            intent.putExtra("intentLongitude", LON);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            Toast.makeText(Login_customer.this, "Successfull Login", Toast.LENGTH_SHORT).show();
//                            startActivity(intent);
//                            lottieAnimationView.cancelAnimation();
//                            lottieAnimationView.setVisibility(View.INVISIBLE);
//
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                Toast.makeText(Login_customer.this, "Auth Failed!", Toast.LENGTH_SHORT).show();
//                Log.d("tag", e.toString());
//                password.getText().clear();
//                lottieAnimationView.cancelAnimation();
//                lottieAnimationView.setVisibility(View.INVISIBLE);
//            }
//        });
//
//    }
//
//
//    @SuppressLint("MissingPermission")
//    private void getLastLocation() {
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                mFusedLocationClient.getLastLocation().addOnCompleteListener(
//                        new OnCompleteListener<Location>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Location> task) {
//                                Location location = task.getResult();
//                                if (location == null) {
//                                    requestNewLocationData();
//                                } else {
//                                    dlat = location.getLatitude();
//                                    LAT = String.valueOf(dlat);
//                                    dlon = location.getLongitude();
//                                    LON = String.valueOf(dlon);
//                                }
//                            }
//                        }
//                );
//            } else {
//                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//            }
//        } else {
//            requestPermissions();
//        }
//    }
//
//
//    @SuppressLint("MissingPermission")
//    private void requestNewLocationData() {
//
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(0);
//        mLocationRequest.setFastestInterval(0);
//        mLocationRequest.setNumUpdates(1);
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        mFusedLocationClient.requestLocationUpdates(
//                mLocationRequest, mLocationCallback,
//                Looper.myLooper()
//        );
//
//    }
//
//    private LocationCallback mLocationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            Location mLastLocation = locationResult.getLastLocation();
//            dlat = mLastLocation.getLatitude();
//            LAT = String.valueOf(dlat);
//            dlon = mLastLocation.getLongitude();
//            LON = String.valueOf(dlon);
//        }
//    };
//
//    private boolean checkPermissions() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        return false;
//    }
//
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(
//                this,
//                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
//                PERMISSION_ID
//        );
//    }
//
//    private boolean isLocationEnabled() {
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//                LocationManager.NETWORK_PROVIDER
//        );
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_ID) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLastLocation();
//            }
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (checkPermissions()) {
//            getLastLocation();
//        }

    }
}
