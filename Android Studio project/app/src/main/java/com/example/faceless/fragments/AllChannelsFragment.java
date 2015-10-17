package com.example.faceless.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.faceless.R;
import com.example.faceless.adapters.ChannelListAdapter;
import com.example.faceless.application.ZApplication;
import com.example.faceless.extras.RequestTags;
import com.example.faceless.gcm.RegistrationIntentService;
import com.example.faceless.objects.ZAllChannelsObject;
import com.example.faceless.preferences.ZPreferences;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AllChannelsFragment extends BaseFragment implements RequestTags {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ZAllChannelsObject obj;
    ChannelListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.all_channels_fragment, container,
                false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycerallchannels);
        setConnectionErrorVariables(v);
        setProgressLayoutVariables(v);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        hideConnectionErrorLayout();
        showProgressLayout();

        StringRequest req = new StringRequest(Method.POST,
                ZApplication.getBaseUrl() + "all_channels/",
                new Listener<String>() {

                    @Override
                    public void onResponse(String res) {
                        obj = new Gson()
                                .fromJson(res, ZAllChannelsObject.class);

                        setAdapterData(obj);

                        Intent intent = new Intent(getActivity(),
                                RegistrationIntentService.class);
                        getActivity().startService(intent);
                    }
                }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                Cache cache = ZApplication.getInstance().getRequestQueue().getCache();
                Cache.Entry entry = cache.get(ZApplication.getBaseUrl() + "all_channels/");
                if (entry != null) {
                    try {
                        String data = new String(entry.data, "UTF-8");
                        obj = new Gson()
                                .fromJson(data, ZAllChannelsObject.class);

                        setAdapterData(obj);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        showConnectionErrorLayout();
                        hideProgressLayout();
                    }
                } else {
                    showConnectionErrorLayout();
                    hideProgressLayout();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_profile_id",
                        ZPreferences.getUserId(getActivity()));
                return params;
            }
        };
        ZApplication.getInstance().addToRequestQueue(req, Z_ALL_CHANNELS);
    }

    private void setAdapterData(final ZAllChannelsObject obj) {
        hideConnectionErrorLayout();
        hideProgressLayout();

        adapter = new ChannelListAdapter(getActivity(), obj);
        recyclerView.setAdapter(adapter);
    }
}
