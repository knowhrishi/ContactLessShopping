package com.example.contactlessshopping.Shops.Main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactlessshopping.Customers.Medical.Medical_MainActivity;
import com.example.contactlessshopping.Customers.ShopDetails;
import com.example.contactlessshopping.Customers.Supermarket.Supermarket_MainActivity;
import com.example.contactlessshopping.R;
import com.example.contactlessshopping.Shops.PhoneAuthLogin.CountryData;
import com.example.contactlessshopping.Shops.PhoneAuthLogin.LoginTemp;
import com.example.contactlessshopping.Shops.ShopMainActivity;
import com.example.contactlessshopping.Shops.ShopRegistration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference noteRef;

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
        DocumentReference docRef;
//                            docRef = fStore.collection("shops").document(Objects.requireNonNull(mAuth.getUid()));
//                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                @Override
//                                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                    if (documentSnapshot.exists()) {
//                                        String shop_category = documentSnapshot.get("shop_category").toString();
//                                        Toast.makeText(ShopLogin.this, ""+ shop_category, Toast.LENGTH_SHORT).show();
//                                        if (shop_category.equals("Grocery Store")) {
//                                            startActivity(new Intent(getApplicationContext(), ShopMainActivity.class));
//                                            finish();
//                                        }
//                                        if (shop_category.equals("Medical Store")) {
//                                            startActivity(new Intent(getApplicationContext(), Medical_MainActivity.class));
//                                            finish();
//                                        }
//                                        if (shop_category.equals("Supermarket")) {
//                                            startActivity(new Intent(getApplicationContext(), Supermarket_MainActivity.class));
//                                            finish();
//                                        }
////                    finish();
//                                    } else {
//                                        //Toast.makeText(Register.this, "Profile Do not Exists.", Toast.LENGTH_SHORT).show();
//
//                                        Intent intent = new Intent(ShopLogin.this, ShopRegistration.class);
//                                        intent.putExtra("intendAuthUID", mAuth.getUid());
//                                        intent.putExtra("intentPhoneNumber", editText.getText().toString());
////
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
////
////                    startActivity(new Intent(getApplicationContext(), ShopRegistration.class));
////                    finish();
//                                    }
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(ShopLogin.this, "Profile Do Not Exists", Toast.LENGTH_SHORT).show();
//                                }
//                            });

                            noteRef = db.collection("shops").document(fAuth.getUid());
                            noteRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                String shop_category = documentSnapshot.getString("shop_category");

                                                Toast.makeText(ShopLogin.this, "" + fAuth.getUid() + "=" + shop_category, Toast.LENGTH_SHORT).show();
                                                if (shop_category.equals("Grocery Store")) {
                                                    startActivity(new Intent(getApplicationContext(), ShopMainActivity.class));
                                                    finish();
                                                }
                                                if (shop_category.equals("Medical Store")) {
                                                    startActivity(new Intent(getApplicationContext(), Medical_MainActivity.class));
                                                    finish();
                                                }
                                                if (shop_category.equals("Supermarket")) {
                                                    startActivity(new Intent(getApplicationContext(), Supermarket_MainActivity.class));
                                                    finish();
                                                }
//                    finish();
                                            } else {
                                                //Toast.makeText(Register.this, "Profile Do not Exists.", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(ShopLogin.this, ShopRegistration.class);
                                                intent.putExtra("intendAuthUID", fAuth.getUid());
                                                intent.putExtra("intentPhoneNumber", phone.getText().toString());
//
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
//
//                    startActivity(new Intent(getApplicationContext(), ShopRegistration.class));
//                    finish();
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
    }
}

//public class ShopLogin extends AppCompatActivity {
//
//
////    private EditText editTextEmail, editTextPassword;
////    private FirebaseAuth auth;
//////    private ProgressDialog progressDialog;
////    LottieAnimationView lottieAnimationView;
////    private Button btnSignup, btnLogin, btnReset;
////    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    //
////    FirebaseAuth fAuth;
////   String otpCode;
////    String verificationId;
////    EditText phone,optEnter;
////    Button next;
////    CountryCodePicker countryCodePicker;
////    PhoneAuthCredential credential;
////    Boolean verificationOnProgress = false;
////    ProgressBar progressBar;
////    TextView state,resend;
////    PhoneAuthProvider.ForceResendingToken token;
//    FirebaseFirestore fStore;
//
//    private String verificationId;
//    FirebaseAuth mAuth;
//    private ProgressBar progressBar;
//    private EditText editText;
//
//    private Spinner spinner;
//    private EditText editText1;
//
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    DocumentReference noteRef;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_shop_login);
//        mAuth = FirebaseAuth.getInstance();
//
//        progressBar = findViewById(R.id.progressbar);
//        editText1 = findViewById(R.id.editTextCode);
//
//
//        spinner = findViewById(R.id.spinnerCountries);
//        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));
//
//        editText = findViewById(R.id.editTextPhone);
//
////
////        phone = findViewById(R.id.phone);
////        optEnter = findViewById(R.id.codeEnter);
////        countryCodePicker = findViewById(R.id.ccp);
////        next = findViewById(R.id.nextBtn);
////        fAuth = FirebaseAuth.getInstance();
//        fStore = FirebaseFirestore.getInstance();
////        progressBar = findViewById(R.id.progressBar);
////        state = findViewById(R.id.state);
////        resend = findViewById(R.id.resendOtpBtn);
////
////        resend.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                // todo:: resend OTP
////            }
////        });
////
////
////        next.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if(!phone.getText().toString().isEmpty() && phone.getText().toString().length() == 10) {
////                    if(!verificationOnProgress){
////                        next.setEnabled(false);
////                        progressBar.setVisibility(View.VISIBLE);
////                        state.setVisibility(View.VISIBLE);
////                        String phoneNum = "+"+countryCodePicker.getSelectedCountryCode()+phone.getText().toString();
////                        Log.d("phone", "Phone No.: " + phoneNum);
////                        requestPhoneAuth(phoneNum);
////                    }else {
////                        next.setEnabled(false);
////                        optEnter.setVisibility(View.GONE);
////                        progressBar.setVisibility(View.VISIBLE);
////                        state.setText("Logging in");
////                        state.setVisibility(View.VISIBLE);
////                        otpCode = optEnter.getText().toString();
////                        if(otpCode.isEmpty()){
////                            optEnter.setError("Required");
////                            return;
////                        }
////
////                        credential = PhoneAuthProvider.getCredential(verificationId,otpCode);
////                        verifyAuth(credential);
////                    }
////
////                }else {
////                    phone.setError("Valid Phone Required");
////                }
////            }
////        });
//
//
//        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String number = editText.getText().toString();
//                Toast.makeText(ShopLogin.this, "number: " + number + "\n" + "ET: " + editText.getText().toString(), Toast.LENGTH_SHORT).show();
//                if (number.isEmpty() || number.length() < 10) {
//                    editText.setError("Valid number is required");
//                    editText.requestFocus();
//                    return;
//                } else {
//                    String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
//                    String phoneNumber = "+" + code + number;
//                    sendVerificationCode(phoneNumber);
//                }
//
//                //String phoneNumber = "+" + code + number;
//
////                Intent intent = new Intent(LoginTemp.this, VerifyPhoneActivity.class);
////                intent.putExtra("phonenumber", phoneNumber);
////                startActivity(intent);
//
//            }
//        });
//
//
//        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String code = editText.getText().toString().trim();
//
//                if (code.isEmpty() || code.length() < 6) {
//
//                    editText.setError("Enter code...");
//                    editText.requestFocus();
//                    return;
//                }
//                verifyCode(code);
//            }
//        });
//    }
//
//
//    private void verifyCode(String code) {
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
//        signInWithCredential(credential);
//    }
//
//    private void signInWithCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//
//                            Toast.makeText(ShopLogin.this, "SIGNIN SUCCESSFULL", Toast.LENGTH_SHORT).show();
////                            checkUserProfile();
////                            DocumentReference docRef;
////                            docRef = fStore.collection("shops").document(Objects.requireNonNull(mAuth.getUid()));
////                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
////                                @Override
////                                public void onSuccess(DocumentSnapshot documentSnapshot) {
////                                    if (documentSnapshot.exists()) {
////                                        String shop_category = documentSnapshot.get("shop_category").toString();
////                                        Toast.makeText(ShopLogin.this, ""+ shop_category, Toast.LENGTH_SHORT).show();
////                                        if (shop_category.equals("Grocery Store")) {
////                                            startActivity(new Intent(getApplicationContext(), ShopMainActivity.class));
////                                            finish();
////                                        }
////                                        if (shop_category.equals("Medical Store")) {
////                                            startActivity(new Intent(getApplicationContext(), Medical_MainActivity.class));
////                                            finish();
////                                        }
////                                        if (shop_category.equals("Supermarket")) {
////                                            startActivity(new Intent(getApplicationContext(), Supermarket_MainActivity.class));
////                                            finish();
////                                        }
//////                    finish();
////                                    } else {
////                                        //Toast.makeText(Register.this, "Profile Do not Exists.", Toast.LENGTH_SHORT).show();
////
////                                        Intent intent = new Intent(ShopLogin.this, ShopRegistration.class);
////                                        intent.putExtra("intendAuthUID", mAuth.getUid());
////                                        intent.putExtra("intentPhoneNumber", editText.getText().toString());
//////
////                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                                        startActivity(intent);
//////
//////                    startActivity(new Intent(getApplicationContext(), ShopRegistration.class));
//////                    finish();
////                                    }
////                                }
////                            }).addOnFailureListener(new OnFailureListener() {
////                                @Override
////                                public void onFailure(@NonNull Exception e) {
////                                    Toast.makeText(ShopLogin.this, "Profile Do Not Exists", Toast.LENGTH_SHORT).show();
////                                }
////                            });
//
//                            noteRef = db.collection("shops").document(mAuth.getUid());
//                            noteRef.get()
//                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                                        @Override
//                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                                            if (documentSnapshot.exists()) {
//                                                String shop_category = documentSnapshot.getString("shop_category");
//
//                                                Toast.makeText(ShopLogin.this, "" + mAuth.getUid() + "=" + shop_category, Toast.LENGTH_SHORT).show();
//                                                if (shop_category.equals("Grocery Store")) {
//                                                    startActivity(new Intent(getApplicationContext(), ShopMainActivity.class));
//                                                    finish();
//                                                }
//                                                if (shop_category.equals("Medical Store")) {
//                                                    startActivity(new Intent(getApplicationContext(), Medical_MainActivity.class));
//                                                    finish();
//                                                }
//                                                if (shop_category.equals("Supermarket")) {
//                                                    startActivity(new Intent(getApplicationContext(), Supermarket_MainActivity.class));
//                                                    finish();
//                                                }
////                    finish();
//                                            } else {
//                                                //Toast.makeText(Register.this, "Profile Do not Exists.", Toast.LENGTH_SHORT).show();
//
//                                                Intent intent = new Intent(ShopLogin.this, ShopRegistration.class);
//                                                intent.putExtra("intendAuthUID", mAuth.getUid());
//                                                intent.putExtra("intentPhoneNumber", editText.getText().toString());
////
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                startActivity(intent);
////
////                    startActivity(new Intent(getApplicationContext(), ShopRegistration.class));
////                    finish();
//                                            }
//                                        }
//                                    })
//                                    .addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//
//                                        }
//                                    });
//
//
//                        } else {
//                            Toast.makeText(ShopLogin.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//    }
//
//    private void sendVerificationCode(String number) {
//        progressBar.setVisibility(View.VISIBLE);
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                number,
//                60,
//                TimeUnit.SECONDS,
//                TaskExecutors.MAIN_THREAD,
//                mCallBack
//        );
//
//    }
//
//    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
//            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//        @Override
//        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            verificationId = s;
//        }
//
//        @Override
//        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//            String code = phoneAuthCredential.getSmsCode();
//            if (code != null) {
//                editText1.setText(code);
//                verifyCode(code);
//            }
//        }
//
//        @Override
//        public void onVerificationFailed(FirebaseException e) {
//            Toast.makeText(ShopLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//    };
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            Toast.makeText(this, "User already logged in - " + FirebaseAuth.getInstance().getUid(), Toast.LENGTH_SHORT).show();
//        }
//    }
//
////
////    private void requestPhoneAuth(String phoneNumber) {
////        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60L, TimeUnit.SECONDS,this,
////                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
////
////                    @Override
////                    public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
////                        super.onCodeAutoRetrievalTimeOut(s);
////                        Toast.makeText(ShopLogin.this,
////                                "OTP Timeout, Please Re-generate the OTP Again.", Toast.LENGTH_SHORT).show();
//////                        resend.setVisibility(View.VISIBLE);
////                    }
////
////                    @Override
////                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
////                        super.onCodeSent(s, forceResendingToken);
////                        verificationId = s;
////                        token = forceResendingToken;
////                        verificationOnProgress = true;
////                        progressBar.setVisibility(View.GONE);
////                        state.setVisibility(View.GONE);
////                        next.setText("Verify");
////                        next.setEnabled(true);
////                        optEnter.setVisibility(View.VISIBLE);
////                    }
////
////                    @Override
////                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
////
////                        // called if otp is automatically detected by the app
////                        verifyAuth(phoneAuthCredential);
////
////                    }
////
////                    @Override
////                    public void onVerificationFailed(@NonNull FirebaseException e) {
////                        Toast.makeText(ShopLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
////
////                    }
////                });
////    }
//
//
////    private void verifyAuth(PhoneAuthCredential credential) {
////        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
////            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
////            @Override
////            public void onComplete(@NonNull Task<AuthResult> task) {
////                if(task.isSuccessful()){
////                    Toast.makeText(ShopLogin.this, "Phone Verified."+ Objects.requireNonNull(fAuth.getCurrentUser()).getUid(), Toast.LENGTH_SHORT).show();
////                    checkUserProfile();
////                }else {
////                    progressBar.setVisibility(View.GONE);
////                    state.setVisibility(View.GONE);
////                    Toast.makeText(ShopLogin.this, "Can not Verify phone and Create Account.", Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
////    }
//
////    @Override
////    protected void onStart() {
////        super.onStart();
////
////        if(fAuth.getCurrentUser() != null){
////            progressBar.setVisibility(View.VISIBLE);
////            state.setText("Checking..");
////            state.setVisibility(View.VISIBLE);
////            checkUserProfile();
////        }
////    }
//
//    private void checkUserProfile() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//        }
//
//    }
//}
