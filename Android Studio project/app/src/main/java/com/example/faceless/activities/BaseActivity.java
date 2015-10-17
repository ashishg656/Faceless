package com.example.faceless.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.faceless.R;

public class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbarTitle;
    int toolbarHeight;

    LinearLayout connectionErrorLayout;
    LinearLayout retryDataConnectionLayout;
    LinearLayout connectionFailedCloudImage;

    LinearLayout progressLayout;
    ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void makeToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void setConnectionErrorVariables() {
        connectionErrorLayout = (LinearLayout) findViewById(R.id.connection_error_layout);
        retryDataConnectionLayout = (LinearLayout) findViewById(R.id.retrylayoutconnectionerror);
        connectionFailedCloudImage = (LinearLayout) findViewById(R.id.connectionfailedimagelayout);
    }

    public void setProgressLayoutVariables() {
        progressLayout = (LinearLayout) findViewById(R.id.progresslayout);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loadinngprogressbar);
    }

    public void showProgressLayout() {
        progressLayout.setVisibility(View.VISIBLE);
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressLayout() {
        progressLayout.setVisibility(View.GONE);
        loadingProgressBar.setVisibility(View.GONE);
    }

    public void showConnectionErrorLayout() {
        connectionErrorLayout.setVisibility(View.VISIBLE);

        connectionFailedCloudImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                retryDataConnectionLayout.performClick();
            }
        });
        findViewById(R.id.opensettingslayout).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                Settings.ACTION_WIFI_SETTINGS);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });
    }

    public void hideConnectionErrorLayout() {
        if (connectionErrorLayout != null)
            connectionErrorLayout.setVisibility(View.GONE);
    }
}
