package com.example.faceless.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.faceless.R;

public class BaseFragment extends Fragment {

    LinearLayout connectionErrorLayout;
    LinearLayout retryDataConnectionLayout;
    LinearLayout connectionFailedCloudImage;

    LinearLayout progressLayout, openSettingsLayout;
    ProgressBar loadingProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected void makeToast(String string) {
        if (getActivity() != null)
            Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }

    public void setConnectionErrorVariables(View v) {
        connectionErrorLayout = (LinearLayout) v.findViewById(R.id.connection_error_layout);
        retryDataConnectionLayout = (LinearLayout) v.findViewById(R.id.retrylayoutconnectionerror);
        connectionFailedCloudImage = (LinearLayout) v.findViewById(R.id.connectionfailedimagelayout);
        openSettingsLayout = (LinearLayout) v.findViewById(R.id.opensettingslayout);
    }

    public void setProgressLayoutVariables(View v) {
        progressLayout = (LinearLayout) v.findViewById(R.id.progresslayout);
        loadingProgressBar = (ProgressBar) v.findViewById(R.id.loadinngprogressbar);
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
        openSettingsLayout.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                Settings.ACTION_WIFI_SETTINGS);
                        if (getActivity() != null && intent.resolveActivity(getActivity().getPackageManager()) != null) {
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
