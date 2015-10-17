package com.example.faceless.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.faceless.R;
import com.example.faceless.activities.SignUpActivity;
import com.example.faceless.application.ZApplication;
import com.example.faceless.extras.RequestTags;
import com.example.faceless.objects.ZCheckTeamObject;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment1 extends BaseFragment implements RequestTags, OnClickListener {

    EditText teamName;
    FrameLayout continueButton;
    TextView introText;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.signup_fragment_1_layout, container,
                false);

        teamName = (EditText) v.findViewById(R.id.teamname);
        continueButton = (FrameLayout) v.findViewById(R.id.continueb);
        introText = (TextView) v.findViewById(R.id.signupfragmentintrotext1);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        introText.setText(getActivity().getResources().getString(R.string.f_signup_fragment_create_team_intro_text));

        continueButton.setOnClickListener(this);
    }

    protected void checkForTeamNameFromServer(final String name) {
        progressDialog = ProgressDialog.show(getActivity(), null, "Checking team", true, false);
        ((SignUpActivity) getActivity()).teamName = name;
        String url = "check_team_exist/";
        StringRequest request = new StringRequest(Method.POST,
                ZApplication.getBaseUrl() + url, new Listener<String>() {

            @Override
            public void onResponse(String res) {
                progressDialog.dismiss();
                ZCheckTeamObject obj = new Gson().fromJson(res,
                        ZCheckTeamObject.class);
                if (obj.isExist()) {
                    makeToast("Team name is already registered with us. Please try again.");
                } else {
                    ((SignUpActivity) getActivity())
                            .setSecondFragment();
                }
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                progressDialog.dismiss();
                makeToast("Some error occured. Check your internet connection.");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("team_name", name);
                return params;
            }
        };
        ZApplication.getInstance().addToRequestQueue(request,
                Z_CHECK_TEAM_EXIST_OR_NOT);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.continueb) {
            String name = teamName.getText().toString();
            if (name.trim().length() > 0) {
                checkForTeamNameFromServer(name.trim());
            } else {
                makeToast("Please enter team name");
            }
        }
    }
}