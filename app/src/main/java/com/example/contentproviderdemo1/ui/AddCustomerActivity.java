package com.example.contentproviderdemo1.ui;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contentproviderdemo1.R;
import com.example.contentproviderdemo1.model.Customer;
import com.example.contentproviderdemo1.model.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddCustomerActivity extends AppCompatActivity {

    TextView textView;
    EditText editTextName,editTextPhone,editTextEmail;
    Button button;
    Customer customer;
    ContentResolver resolver;
    boolean updateMode;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    FirebaseFirestore db;

    void initViews(){
        resolver = getContentResolver();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = auth.getCurrentUser();

        textView = findViewById(R.id.textView);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        button = findViewById(R.id.buttonSubmit);

        customer = new Customer();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customer.name = editTextName.getText().toString();
                customer.phone = editTextPhone.getText().toString();
                customer.email = editTextEmail.getText().toString();
                //addCustomerInDB();
                if(Util.isInternetConnected(AddCustomerActivity.this)) {
                    saveCustomerInCloudDB();
                }else{
                    // give message
                }
            }
        });

        Intent rcv = getIntent();
        updateMode = rcv.hasExtra("keyCustomer");

        if(updateMode){
            customer = (Customer) rcv.getSerializableExtra("keyCustomer");
            editTextName.setText(customer.name);
            editTextPhone.setText(customer.phone);
            editTextEmail.setText(customer.email);
            button.setText("Update Customer");
        }

    }

    void addCustomerInDB()
    {
        ContentValues values = new ContentValues();
        values.put(Util.COL_NAME,customer.name);
        values.put(Util.COL_PHONE,customer.phone);
        values.put(Util.COL_EMAIL,customer.email);
        if (updateMode){

                String where = Util.COL_ID+" = "+customer.id;
        int i = resolver.update(Util.CUSTOMER_URI,values,where,null);
        if(i>0){
            Toast.makeText(this,"Updation Finished",Toast.LENGTH_LONG).show();

            finish();
        }else{
            Toast.makeText(this,"Updation Failed",Toast.LENGTH_LONG).show();
        }

        }else{
        Uri uri = resolver.insert(Util.CUSTOMER_URI, values);
        Toast.makeText(this,customer.name+" Added in Database: "+uri.getLastPathSegment(),Toast.LENGTH_LONG).show();

        clearFields();
    }
        clearFields();
    }

    void saveCustomerInCloudDB(){

        if(updateMode){

            db.collection("users").document(firebaseUser.getUid())
                    .collection("customers").document(customer.docId)
                    .set(customer)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if(task.isComplete()){
                                Toast.makeText(AddCustomerActivity.this,"Updation Finished",Toast.LENGTH_LONG).show();
                                finish();
                            }else{
                                Toast.makeText(AddCustomerActivity.this,"Updation Failed",Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }else {

            db.collection("users").document(firebaseUser.getUid())
                    .collection("customers").add(customer)
                    .addOnCompleteListener(this, new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(Task<DocumentReference> task) {
                            if (task.isComplete()) {
                                Toast.makeText(AddCustomerActivity.this, "Customer Saved in DB", Toast.LENGTH_LONG).show();
                                clearFields();
                            }
                        }
                    });
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        getSupportActionBar().setTitle("Blue Pop");
        initViews();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1,101,1,"All Customers");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == 101){
          Intent intent = new Intent(AddCustomerActivity.this, AllCustomersActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    void clearFields(){
        editTextName.setText("");
        editTextPhone.setText("");
        editTextEmail.setText("");

    }

}
