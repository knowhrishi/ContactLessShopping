package com.example.contactlessshopping.Shops;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.contactlessshopping.MainActivity;
import com.example.contactlessshopping.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShopRegistration extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private static final String TAG = "ShopRegistration";
    public static final String KEY_SHOPNAME = "shop_name";
    public static final String KEY_PHONE = "phone_number";
    public static final String KEY_EMAIL = "email_id";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CAPACITY = "capacity";
    public static final String KEY_SHOP_CATEGORY = "shop_category";
    public static final String KEY_FROM_TIME = "from_time";
    public static final String KEY_TO_TIME = "to_time";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_ADDRESS = "address";

    private EditText editTextShopName, editTextFromTime, editTextToTime, editTextCapacity, editTextPhoneNumber, editTextEmail, editTextPassword;
    private RadioGroup radioTypeGroup;
    private RadioButton radioButton;
    private RadioButton radioButtonYes, radioButtonNo;
    private ProgressDialog progressDialog;
    MaterialSpinner categorySpinner;
    private Button timeFrom, timeTo, buttonShopRegister,buttonAddSlot;
    public String insertSlot, authid;
    private String LONGITUDE, LATITUDE, shop_name, phone_number, email_id, password, capacity, from_time, to_time, address = "NULL", shop_category;


    FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore db2 = FirebaseFirestore.getInstance();

    Map<String,Object> slots=new HashMap<>();
    List<String> empty_array= Collections.<String>emptyList();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_registration);

        editTextShopName = (EditText) findViewById(R.id.idShopName);
        editTextFromTime = (EditText) findViewById(R.id.idEtFromTime);
        editTextToTime = (EditText) findViewById(R.id.idEtToTime);
        editTextCapacity = (EditText) findViewById(R.id.idEtCapacity);
        editTextEmail = (EditText) findViewById(R.id.idEmail);
        editTextPassword = (EditText) findViewById(R.id.idPassword);
        editTextPhoneNumber = (EditText) findViewById(R.id.idPhoneNumber);

        progressDialog = new ProgressDialog(this);


        radioTypeGroup = (RadioGroup) findViewById(R.id.radioSex);
        radioButtonYes = (RadioButton) findViewById(R.id.radioButtonYes);
        radioButtonNo = (RadioButton) findViewById(R.id.radioButtonNo);

        timeFrom = (Button) findViewById(R.id.idBtnFromTime);
        timeTo = (Button) findViewById(R.id.idBtnToTime);
        buttonAddSlot=findViewById(R.id.add_slot_bt);

        buttonShopRegister = (Button) findViewById(R.id.ShopRegister);
        auth = FirebaseAuth.getInstance();


        timeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ShopRegistration.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //editTextFromTime.setText(selectedHour + ":" + selectedMinute);
                        editTextFromTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        timeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ShopRegistration.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        editTextToTime.setText(selectedHour + ":" + selectedMinute);
                        editTextToTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });







//
//               System.out.println(authid + "\n");;
 //               String[] itemsSlots = insertSlot.split(",");
              //  String item;
//                for (String item : itemsSlots) {
//                    String noticeID =  UUID.randomUUID().toString().replaceAll("-", "");
//
//                    System.out.println("item = " + item + "\n" + "UID:" + noticeID);
//                }
//


        buttonAddSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date c = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(c);
                String dateInString = "2011-11-30";  // Start date
                SimpleDateFormat sdf_incr_date = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c1 = Calendar.getInstance();
                try {
                    c1.setTime(sdf_incr_date.parse(formattedDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c1.add(Calendar.DATE, 1);
                sdf_incr_date = new SimpleDateFormat("dd/MM/yyyy");
                Date resultdate = new Date(c1.getTimeInMillis());
                dateInString = sdf_incr_date.format(resultdate);
                Toast.makeText(ShopRegistration.this, formattedDate + "\n" + dateInString, Toast.LENGTH_SHORT).show();
                String date1 = formattedDate;
                String time1 = editTextFromTime.getText().toString();
                String date2 = dateInString;
                String time2 = editTextToTime.getText().toString();
                String format = "dd/MM/yyyy HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                try {
                    Date dateObj1 = sdf.parse(date1 + " " + time1);
                    Date dateObj2 = sdf.parse(date1 + " " + time2);
                    Log.d("TAG", "Date Start: " + dateObj1);
                    Log.d("TAG", "Date End: " + dateObj2);
                    long dif = dateObj1.getTime();
                    while (dif < dateObj2.getTime()) {
                        Date slot1 = new Date(dif);
                        dif += 3600000;
                        if(dif+3600000>dateObj2.getTime())
                        {
                            dif=dateObj2.getTime();
                        }
                        Date slot2 = new Date(dif);


                        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
                        Log.d("TAG", sdf1.format(slot1) + " - " + sdf2.format(slot2));
                        insertSlot = insertSlot + "," + sdf1.format(slot1) + " - " + sdf2.format(slot2);
                        slots.put(sdf1.format(slot1) + " - " + sdf2.format(slot2),empty_array);
                    }
                    slots.put("date",date1);
                }catch (ParseException ex){
                    ex.printStackTrace();
                }

               /* for(int i = 1;i<10;i++){
                    String noticeID =  UUID.randomUUID().toString().replaceAll("-", "");
                    authid = authid + "," + noticeID + ',' + authid;
                }*/

            }
        });






        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    ShopRegistration.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        }

        radioButtonYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                    radioButtonNo.setChecked(true);
                    Toast.makeText(ShopRegistration.this, "Sorry, this function is under work. Try again later", Toast.LENGTH_SHORT).show();
                    radioButtonYes.setChecked(false);
                }
            }
        });
        radioButtonNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    radioButtonYes.setChecked(false);

                    if (ContextCompat.checkSelfPermission(
                            getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(
                                ShopRegistration.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE_LOCATION_PERMISSION
                        );
                    } else {
                        getCurrentLocation();
                    }
                }
            }
        });

        categorySpinner = (MaterialSpinner) findViewById(R.id.spinnerCategory);
        categorySpinner.setItems("Groceries", "Medical","SuperMarket");
        categorySpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                shop_category = item;
            }
        });


        buttonShopRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                shop_name = editTextShopName.getText().toString();
                phone_number = editTextPassword.getText().toString();
                email_id = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                capacity = editTextCapacity.getText().toString();
                from_time = editTextFromTime.getText().toString();
                to_time = editTextToTime.getText().toString();
                //address               = editTextToTime.getText().toString();
//                dob                     = editTextDOB.getText().toString();


                if (TextUtils.isEmpty(shop_name)) {
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(capacity)) {
                    Toast.makeText(getApplicationContext(), "Enter last name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phone_number)) {
                    Toast.makeText(getApplicationContext(), "Enter Phone Number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email_id)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }


                progressDialog.setMessage("Registering ....");
                progressDialog.show();
                auth.createUserWithEmailAndPassword(email_id, password)
                        .addOnCompleteListener(ShopRegistration.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (!task.isSuccessful()) {
                                    Toast.makeText(ShopRegistration.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    Map<String, Object> note = new HashMap<>();
                                    note.put(KEY_SHOPNAME, shop_name);
                                    note.put(KEY_PHONE, phone_number);
                                    note.put(KEY_EMAIL, email_id);
                                    note.put(KEY_PASSWORD, password);
                                    note.put(KEY_CAPACITY, capacity);
                                    note.put(KEY_FROM_TIME, from_time);
                                    note.put(KEY_TO_TIME, to_time);
                                    note.put(KEY_LONGITUDE, LONGITUDE);
                                    note.put(KEY_LATITUDE, LATITUDE);
                                    note.put(KEY_ADDRESS, address);
                                    note.put(KEY_SHOP_CATEGORY, shop_category);
                                    progressDialog.setMessage("Registering....");
                                    progressDialog.show();


                                    Toast.makeText(ShopRegistration.this,auth.getUid() +" in main",Toast.LENGTH_SHORT).show();
                                    db.collection("shops").document(auth.getUid()).set(note)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(ShopRegistration.this, "Data saved", Toast.LENGTH_SHORT).show();

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    //Toast.makeText(PatientRegister.this, "Error!", Toast.LENGTH_SHORT).show();
                                                    Log.d(TAG, e.toString());
                                                }
                                            });


                                    if(shop_category=="SuperMarket")
                                    {
                                        slots.put("shop_id",auth.getUid());
                                        Toast.makeText(ShopRegistration.this,auth.getUid()+" in cat",Toast.LENGTH_SHORT).show();
                                        db.collection("tokens").document(auth.getUid()).set(slots)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        //Toast.makeText(PatientRegister.this, "Data saved", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(ShopRegistration.this, MainActivity.class));
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        //Toast.makeText(PatientRegister.this, "Error!", Toast.LENGTH_SHORT).show();
                                                        Log.d(TAG, e.toString());
                                                    }
                                                });

                                    }


                                }
                            }
                        });





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

    private void getCurrentLocation() {

        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(ShopRegistration.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(ShopRegistration.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int lastestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude =
                                    locationResult.getLocations().get(lastestLocationIndex).getLatitude();
                            double longitude =
                                    locationResult.getLocations().get(lastestLocationIndex).getLongitude();

                            LATITUDE = String.format(String.valueOf(latitude));
                            LONGITUDE = String.format(String.valueOf(longitude));

                        }
                    }
                }, Looper.getMainLooper());

    }

}

