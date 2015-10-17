package com.example.faceless.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.faceless.R;
import com.example.faceless.activities.ZCreatePostActivity;
import com.example.faceless.adapters.PostListAdapter;
import com.example.faceless.application.ZApplication;
import com.example.faceless.extras.RequestTags;
import com.example.faceless.objects.ZPostListObject;
import com.example.faceless.preferences.ZPreferences;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PostsFragment extends BaseFragment implements RequestTags, View.OnClickListener {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    PostListAdapter adapter;
    boolean isMoreAllowed;
    boolean isRequestRunning;
    Integer nextPage = 1;
    FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.posts_fragment_layout, container,
                false);

        setConnectionErrorVariables(v);
        setProgressLayoutVariables(v);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycerallchannels);
        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.addbutton);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        retryDataConnectionLayout.setOnClickListener(this);

        hideConnectionErrorLayout();
        showProgressLayout();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getAdapter() != null) {
                    int lastitem = layoutManager.findLastVisibleItemPosition();
                    int totalitems = recyclerView.getAdapter().getItemCount();
                    int diff = totalitems - lastitem;
                    if (diff < 6 && !isRequestRunning && isMoreAllowed) {
                        loadData();
                    }
                }
            }
        });

        floatingActionButton.setOnClickListener(this);

        loadData();
    }

    private void loadData() {
        isRequestRunning = true;
        if (adapter == null) {
            showProgressLayout();
            hideConnectionErrorLayout();
        }
        StringRequest req = new StringRequest(Request.Method.POST,
                ZApplication.getBaseUrl() + "get_feeds/?pagenumber=" + nextPage,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String res) {
                        isRequestRunning = false;
                        ZPostListObject obj = new Gson().fromJson(res,
                                ZPostListObject.class);
                        setAdapterData(obj);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                isRequestRunning = false;
                Cache cache = ZApplication.getInstance().getRequestQueue().getCache();
                Cache.Entry entry = cache.get(ZApplication.getBaseUrl() + "get_feeds/?pagenumber=" + nextPage);
                if (entry != null) {
                    try {
                        String data = new String(entry.data, "UTF-8");
                        ZPostListObject obj = new Gson().fromJson(data,
                                ZPostListObject.class);
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
        ZApplication.getInstance().addToRequestQueue(req, Z_CHATS);
    }

    private void setAdapterData(ZPostListObject obj) {
        hideConnectionErrorLayout();
        hideProgressLayout();
        nextPage = obj.getNext_page();
        if (obj.getNext_page() == null)
            isMoreAllowed = false;
        else
            isMoreAllowed = true;
        if (adapter == null) {
            adapter = new PostListAdapter(obj.getPosts_array(), isMoreAllowed, getActivity());
            recyclerView.setAdapter(adapter);
        } else {
            adapter.addItemsToListAtEnd(obj.getPosts_array(), isMoreAllowed);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.retrylayoutconnectionerror) {
            loadData();
        } else if (v.getId() == R.id.addbutton) {
            String[] items = {"Create Post", "Create Poll"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add a discussion").setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which==0){
                        Intent i = new Intent(getActivity(), ZCreatePostActivity.class);
                        getActivity().startActivity(i);
                    }
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
