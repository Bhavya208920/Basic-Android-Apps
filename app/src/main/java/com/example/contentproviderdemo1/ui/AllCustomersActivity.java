package com.example.contentproviderdemo1.ui;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.contentproviderdemo1.Adapter.CustomersAdapter;
import com.example.contentproviderdemo1.R;
import com.example.contentproviderdemo1.listener.OnRecyclerItemClickListener;
import com.example.contentproviderdemo1.model.Customer;
import com.example.contentproviderdemo1.model.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllCustomersActivity extends AppCompatActivity implements OnRecyclerItemClickListener {

    ContentResolver resolver;
    RecyclerView recyclerView;
    ArrayList<Customer> customers;
    CustomersAdapter customersAdapter;
    int position;

    Customer customer;

    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;

    void initViews(){
        resolver = getContentResolver();
        recyclerView = findViewById(R.id.recyclerView);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = auth.getCurrentUser();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_customers);
        getSupportActionBar().setTitle("Blue Pop");
        initViews();
        //fetchCustomersFromDB();

        if(Util.isInternetConnected(this)) {
            fetchCustomersFromCloudDB();
        }else{
            Toast.makeText(AllCustomersActivity.this,"Please Connect to Internet and Try Again",Toast.LENGTH_LONG).show();
        }
    }

    void fetchCustomersFromCloudDB(){

        db.collection("users").document(firebaseUser.getUid())
                .collection("customers").get()
                .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if(task.isComplete()){

                            customers = new ArrayList<>();

                            QuerySnapshot querySnapshot = task.getResult();
                            List<DocumentSnapshot> documentSnapshots = querySnapshot.getDocuments();

                            for(DocumentSnapshot snapshot : documentSnapshots){
                                String docId = snapshot.getId();
                                Customer customer = snapshot.toObject(Customer.class);
                                customer.docId = docId;
                                customers.add(customer);
                            }

                            getSupportActionBar().setTitle("Total Customers: "+customers.size());

                            customersAdapter = new CustomersAdapter(AllCustomersActivity.this,R.layout.list_item,customers);

                            customersAdapter.setOnRecyclerItemClickListener(AllCustomersActivity.this);

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AllCustomersActivity.this);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(customersAdapter);

                        }else{
                            Toast.makeText(AllCustomersActivity.this,"Some Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
    void fetchCustomersFromDB(){

        String[] projection = {Util.COL_ID, Util.COL_NAME, Util.COL_PHONE, Util.COL_EMAIL};
        Cursor cursor = resolver.query(Util.CUSTOMER_URI, projection, null, null, null);

        if(cursor!=null){

            customers = new ArrayList<>();

            while(cursor.moveToNext()){

                Customer customer = new Customer();
                customer.id = cursor.getInt(cursor.getColumnIndex(Util.COL_ID));
                customer.name = cursor.getString(cursor.getColumnIndex(Util.COL_NAME));
                customer.phone = cursor.getString(cursor.getColumnIndex(Util.COL_PHONE));
                customer.email = cursor.getString(cursor.getColumnIndex(Util.COL_EMAIL));

                customers.add(customer);
            }

            getSupportActionBar().setTitle("Total Customers: "+customers.size());

            customersAdapter = new CustomersAdapter(this,R.layout.list_item,customers);
            customersAdapter.setOnRecyclerItemClickListener(this);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
           recyclerView.setAdapter(customersAdapter);
        }

    }

    void showCustomerDetails(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(customer.name+" Details:");
        builder.setMessage(customer.toString());
        builder.setPositiveButton("Done",null);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    void deleteCustomerFromDB(){
        String where = Util.COL_ID+" = "+customer.id;
        int i = resolver.delete(Util.CUSTOMER_URI,where,null);
        if(i>0){
            Toast.makeText(this,"Deletion Finished",Toast.LENGTH_LONG).show();
            customers.remove(position);
            customersAdapter.notifyDataSetChanged(); // Refresh Your RecyclerView
        }else{
            Toast.makeText(this,"Deletion Failed",Toast.LENGTH_LONG).show();
        }
    }
    void deleteCustomerFromCloudDB(){
        db.collection("users").document(firebaseUser.getUid())
                .collection("customers").document(customer.docId)
                .delete()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if(task.isComplete()){
                            Toast.makeText(AllCustomersActivity.this,"Deletion Finished",Toast.LENGTH_LONG).show();
                            customers.remove(position);
                            customersAdapter.notifyDataSetChanged(); // Refresh Your RecyclerView
                        }else{
                            Toast.makeText(AllCustomersActivity.this,"Deletion Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    void askForDeletion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete "+customer.name);
        builder.setMessage("Are You Sure ?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // deleteCustomerFromDB();
                deleteCustomerFromCloudDB();

            }
        });
        builder.setNegativeButton("Cancel",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

      void showOptions()
          {
              AlertDialog.Builder builder = new AlertDialog.Builder(this);
              String[] items = {"View "+customer.name , "Update "+customer.name , "Delete "+customer.name , "Cancel"};
              builder.setItems(items, new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {

                      switch (which){

                          case 0:
                              showCustomerDetails();
                              break;

                          case 1:
                              Intent intent = new Intent(AllCustomersActivity.this, AddCustomerActivity.class);
                              intent.putExtra("keyCustomer",customer);

                              startActivity(intent);

                              break;

                          case 2:
                              askForDeletion();

                              break;

                          case 3:
                              break;
                      }
                  }
              });

              AlertDialog dialog = builder.create();
              dialog.show();
          }
    @Override
    public void onItemClick(int position) {
        this.position = position;
        customer = customers.get(position);
        Toast.makeText(this,"You Clicked on Position:"+position,Toast.LENGTH_LONG).show();
        showOptions();
    }
}
