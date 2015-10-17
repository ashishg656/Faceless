package com.example.faceless.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.faceless.R;
import com.example.faceless.fragments.SignUpFragment1;
import com.example.faceless.fragments.SignUpFragment2;

public class SignUpActivity extends BaseActivity {

    public String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setFirstFragment();
    }

    private void setFirstFragment() {
        getSupportActionBar().setTitle("Enter Team Name");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentcon, new SignUpFragment1()).commit();
    }

    @Override
    public void onBackPressed() {
        getSupportActionBar().setTitle("Enter Team Name");
        super.onBackPressed();
    }

    public void setSecondFragment() {
        getSupportActionBar().setTitle("Create Admin Account");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentcon, new SignUpFragment2())
                .addToBackStack("Ad").commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
