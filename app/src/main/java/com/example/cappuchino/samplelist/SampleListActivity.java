package com.example.cappuchino.samplelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class SampleListActivity extends AppCompatActivity {

    public static final String EMPTY_MODE_KEY = "EMPTY_MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == savedInstanceState) {
            Intent intent = getIntent();
            boolean empty_mode = intent.getBooleanExtra(EMPTY_MODE_KEY, false);
            initFragment(SampleListFragment.newInstance(empty_mode));
        }
    }

    private void initFragment(Fragment sampleFragment) {
        String tag = SampleListFragment.class.getSimpleName();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(android.R.id.content, sampleFragment, tag);
        transaction.commit();
    }

}
