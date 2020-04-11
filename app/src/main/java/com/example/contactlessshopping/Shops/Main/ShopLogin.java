package com.example.contactlessshopping.Shops.Main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.ShopMainActivity;
import com.example.contactlessshopping.Shops.ShopRegistration;
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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ShopLogin extends AppCompatActivity {


//    private EditText editTextEmail, editTextPassword;
//    private FirebaseAuth auth;
////    private ProgressDialog progressDialog;
//    LottieAnimationView lottieAnimationView;
//    private Button btnSignup, btnLogin, btnReset;
//    private FirebaseFirestore db = FirebaseFirestore.getInstance();


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
        setContentView(R.layout.activity_shop_login);

//        auth = FirebaseAuth.getInstance();
////        if (auth.getCurrentUser() != null) {
////            startActivity(new Intent(ShopLogin.this, ShopMainActivity.class));
////            finish();
////        }
//
//        editTextEmail      = (EditText) findViewById(R.id.email);
//        editTextPassword   = (EditText) findViewById(R.id.password);
////        progressDialog = new ProgressDialog(this);
//        lottieAnimationView=findViewById(R.id.loading);
//        btnSignup          = (Button) findViewById(R.id.btn_signup);
//        btnLogin           = (Button) findViewById(R.id.btn_login);
//
//
//
//        //btnReset           = (Button) findViewById(R.id.btn_reset_password);
//
//
//
//        btnSignup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ShopLogin.this, ShopRegistration.class));
//            }
//        });
//
//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = editTextEmail.getText().toString();
//                final String password = editTextPassword.getText().toString();
//
//                if (TextUtils.isEmpty(email)) {
//                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(password)) {
//                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
////                progressDialog.setMessage("Please Wait...");
////                progressDialog.show();
//                lottieAnimationView.setVisibility(View.VISIBLE);
//                lottieAnimationView.playAnimation();
//                //authenticate user
//                auth.signInWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(ShopLogin.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
////
//                                lottieAnimationView.cancelAnimation();
//                                lottieAnimationView.setVisibility(View.INVISIBLE);
////                                progressDialog.dismiss();
//                                if (!task.isSuccessful()) {
//                                    // there was an error
//                                    if (password.length() < 6) {
//                                        editTextPassword.setError("Password should be greater than 6 digits");
//                                    }else {
//                                        Toast.makeText(ShopLogin.this, "Auth Fail", Toast.LENGTH_LONG).show();
//                                    }
//                                } else {
//                                    Intent intent = new Intent(ShopLogin.this, ShopMainActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                    lottieAnimationView.cancelAnimation();
//                                    lottieAnimationView.setVisibility(View.INVISIBLE);
//                                }
//                            }
//                        });
//            }
//        });

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
                        Toast.makeText(ShopLogin.this,
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
                        Toast.makeText(ShopLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


    private void verifyAuth(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ShopLogin.this, "Phone Verified."+ Objects.requireNonNull(fAuth.getCurrentUser()).getUid(), Toast.LENGTH_SHORT).show();
                    checkUserProfile();
                }else {
                    progressBar.setVisibility(View.GONE);
                    state.setVisibility(View.GONE);
                    Toast.makeText(ShopLogin.this, "Can not Verify phone and Create Account.", Toast.LENGTH_SHORT).show();
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
                    startActivity(new Intent(getApplicationContext(), ShopMainActivity.class));
                    finish();
                }else {
                    //Toast.makeText(Register.this, "Profile Do not Exists.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ShopRegistration.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShopLogin.this, "Profile Do Not Exists", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
