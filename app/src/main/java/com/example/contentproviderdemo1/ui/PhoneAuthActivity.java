package com.example.contentproviderdemo1.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.contentproviderdemo1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity implements View.OnClickListener {
 EditText editTextPhone;//,editTextOTP;
 Button buttonVerify;
 PhoneAuthProvider provider;
 FirebaseAuth auth;
 void initViews()
 {
     editTextPhone = findViewById(R.id.editTextPhone);
   //  editTextOTP = findViewById(R.id.editTextOTP);
     buttonVerify = findViewById(R.id.buttonVerify);
     buttonVerify.setOnClickListener(this);
     provider = PhoneAuthProvider.getInstance();
     auth = FirebaseAuth.getInstance();
 }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        getSupportActionBar().setTitle("Phone Authentication");
        initViews();

    }

    @Override
    public void onClick(View v) {
        String phone = editTextPhone.getText().toString().trim();

        provider.verifyPhoneNumber(
                phone,
                60,
                TimeUnit.SECONDS,
              PhoneAuthActivity.this,callbacks);

    };
PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    @Override
    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

    }

    @Override
    public void onVerificationFailed(FirebaseException e) {
        Toast.makeText(PhoneAuthActivity.this,"OTP Verification Failed" +e.getMessage(),Toast.LENGTH_LONG).show();
    }
};
    void signInUser(PhoneAuthCredential phoneAuthCredential) {
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isComplete()) {
                    FirebaseUser user = task.getResult().getUser();
                    String userId = user.getUid();
                }
            }
        });
    }

}
