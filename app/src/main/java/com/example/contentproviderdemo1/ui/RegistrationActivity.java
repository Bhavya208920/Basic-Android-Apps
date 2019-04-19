package com.example.contentproviderdemo1.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contentproviderdemo1.HomeActivity;
import com.example.contentproviderdemo1.MainActivity;
import com.example.contentproviderdemo1.R;
import com.example.contentproviderdemo1.model.User;
import com.example.contentproviderdemo1.model.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText eTxtName, eTxtEmail, eTxtPassword;
    TextView txtLogin;
    Button btnRegister;
    User user;

    // To Access FirebaseAuth WebService
    FirebaseAuth auth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    FirebaseUser firebaseUser;

    void initViews(){
        eTxtName = findViewById(R.id.editTextName);
        eTxtEmail = findViewById(R.id.editTextEmail);
        eTxtPassword = findViewById(R.id.editTextPassword);
        btnRegister = findViewById(R.id.buttonRegister);
        txtLogin = findViewById(R.id.textViewLogin);

        btnRegister.setOnClickListener(this);
        txtLogin.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        user = new User();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setTitle("Blue Pop");
        initViews();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if(id == R.id.buttonRegister) {

            //Get the data from UI and put it into User Object
            user.name = eTxtName.getText().toString();
            user.email = eTxtEmail.getText().toString();
            user.password = eTxtPassword.getText().toString();

            if(Util.isInternetConnected(this)) {
                registerUser();
            }else{
                Toast.makeText(this,"Please Connect to Internet and Try Again",Toast.LENGTH_LONG).show();
            }
        }else{
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
    void registerUser(){

        progressDialog.show();

        auth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isComplete()){
                            /*Toast.makeText(RegistrationActivity.this,user.name+ "Registered Sucessfully", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                            Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();*/
                            saveUserInCloudDB();
                        }
                    }
                });
    }

    void saveUserInCloudDB(){

        /*db.collection("users").add(user)
        .addOnCompleteListener(this, new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isComplete()){
                     Toast.makeText(RegistrationActivity.this,user.name+ "Registered Sucessfully", Toast.LENGTH_LONG).show();
                     progressDialog.dismiss();
                     Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                     startActivity(intent);
                     finish();
                }
            }
        });*/

        firebaseUser = auth.getCurrentUser();
        db.collection("users").document(firebaseUser.getUid()).set(user)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        Toast.makeText(RegistrationActivity.this,user.name+ "Registered Sucessfully", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                        Intent intent = new Intent(RegistrationActivity.this, AddCustomerActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}