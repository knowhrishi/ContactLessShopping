package com.example.contactlessshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.contactlessshopping.Shops.Main.ShopLogin;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;

public class admin extends AppCompatActivity {

    Button reset,reform;
    private FirebaseFunctions mFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        reset=findViewById(R.id.reset);
        reform=findViewById(R.id.reform);
        mFunctions = FirebaseFunctions.getInstance();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Task<String> t = resetslots();
                  /*      .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            if (e instanceof FirebaseFunctionsException) {
                                FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                FirebaseFunctionsException.Code code = ffe.getCode();
                                Object details = ffe.getDetails();
                                Log.d("Exception while reset:",details.toString());
                            }


                        }
                        else
                        {

                            Toast.makeText(admin.this,"Successfull reset",Toast.LENGTH_SHORT).show();
                        }

                    }
                });*/
                Log.d("Reset ","done");

            }
        });

        reform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Reform ","done");

            }
        });
    }

    private Task<String> resetslots() {
        // Create the arguments to the callable function.
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","test");

        Log.d("in","in reset");
        return mFunctions
                .getHttpsCallable("resetslots")
                .call(data)
                .continueWith(new Continuation<HttpsCallableResult, String>() {
                    @Override
                    public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        // This continuation runs on either success or failure, but if the task
                        // has failed then getResult() will throw an Exception which will be
                        // propagated down.

                        String result = (String) task.getResult().getData();
                        Log.d("in","Successfull"+result);
                        return result;
                    }
                });
    }
}
