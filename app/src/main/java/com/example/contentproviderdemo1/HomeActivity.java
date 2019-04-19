package com.example.contentproviderdemo1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.contentproviderdemo1.ui.MyFragmentsActivity;
import com.example.contentproviderdemo1.ui.TabsActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Blue Pop");
        auth = FirebaseAuth.getInstance();
        textView2 = findViewById(R.id.textView2);
        textView2.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(1, 101, 0, "Logout");

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == 101){
            auth.signOut(); // Clear Logged In Users Data from Shared Preferences
            Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(HomeActivity.this, MyFragmentsActivity.class);
        startActivity(intent);
    }
}
