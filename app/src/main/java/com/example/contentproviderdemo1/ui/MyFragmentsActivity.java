package com.example.contentproviderdemo1.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.contentproviderdemo1.R;
import com.example.contentproviderdemo1.UpperFragment;

public class MyFragmentsActivity extends AppCompatActivity {

    UpperFragment upperFragment;
    FragmentManager fragmentManager;

    void initViews()
    {
        upperFragment = new UpperFragment();
        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().add(R.id.upperFrame,upperFragment).commit();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fragments);
        getSupportActionBar().hide();
        initViews();
    }
}
