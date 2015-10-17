package com.example.faceless.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.faceless.R;
import com.example.faceless.adapters.ChatListAdapter;
import com.example.faceless.application.ZApplication;
import com.example.faceless.extras.RequestTags;
import com.example.faceless.objects.ZChatObject;
import com.example.faceless.preferences.ZPreferences;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends BaseActivity implements RequestTags, OnClickListener {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    int channelId;
    public boolean isMoreAllowed, isRequestRunning;
    Integer nextPage = 1;
    ChatListAdapter adapter;
    EditText messageBox;
    public static boolean isChatActivityResumed;
    public static ChatActivity chatActivityInstance;
    FloatingActionButton sendButton;

    @Override
    protected void onResume() {
        isChatActivityResumed = true;
        chatActivityInstance = this;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isChatActivityResumed = false;
        chatActivityInstance = null;
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity_layout);

        setConnectionErrorVariables();
        setProgressLayoutVariables();
        toolbar=(Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getExtras().getString("channel_name"));

        sendButton = (FloatingActionButton) findViewById(R.id.sendbutton);
        sendButton.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
        messageBox = (EditText) findViewById(R.id.messagetosend);

        layoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);

        channelId = getIntent().getExtras().getInt("channel_id");

        recyclerView.addOnScrollListener(new OnScrollListener() {

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

        retryDataConnectionLayout.setOnClickListener(this);

        loadData();
    }

    public void addmessageToChatsListWhenReceivedThroughtGCM(String message) {
        try {
            ZChatObject.ChatItem item = new ZChatObject().new ChatItem();

            item.setText(message);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            item.setTime(sdf.format(date));

            adapter.addItemToListAtBeggining(item);
            int currentPos = layoutManager.findFirstVisibleItemPosition();
            layoutManager.scrollToPosition(currentPos + 1);
        } catch (Exception e) {

        }
    }

    protected void sendMessageToServer() {
        sendButton.setClickable(false);
        final String message = messageBox.getText().toString().trim();
        StringRequest req = new StringRequest(Method.POST,
                ZApplication.getBaseUrl() + "add_chat_message/",
                new Listener<String>() {

                    @Override
                    public void onResponse(String arg0) {
                        sendButton.setClickable(true);
                        Log.w("as", "chat send message response received");
                        ZChatObject.ChatItem item = new ZChatObject().new ChatItem();

                        item.setText(message);
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        item.setTime(sdf.format(date));

                        adapter.addItemToListAtBeggining(item);
                        layoutManager.scrollToPosition(0);
                        messageBox.setText("");
                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                sendButton.setClickable(true);
                makeToast("Unable to send message.Try again");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("message", message);
                params.put("channel_id", channelId + "");
                params.put("user_profile_id",
                        ZPreferences.getUserId(ChatActivity.this));
                return params;
            }
        };
        ZApplication.getInstance().addToRequestQueue(req, Z_ADD_CHAT);
    }

    private void loadData() {
        isRequestRunning = true;
        if (adapter == null) {
            showProgressLayout();
            hideConnectionErrorLayout();
        }
        StringRequest req = new StringRequest(Method.POST,
                ZApplication.getBaseUrl() + "get_chats_for_channel/?pagenumber=" + nextPage + "&channel_id=" + channelId,
                new Listener<String>() {

                    @Override
                    public void onResponse(String res) {
                        isRequestRunning = false;
                        ZChatObject obj = new Gson().fromJson(res,
                                ZChatObject.class);
                        setAdapterData(obj);
                    }
                }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                isRequestRunning = false;
                Cache cache = ZApplication.getInstance().getRequestQueue().getCache();
                Cache.Entry entry = cache.get(ZApplication.getBaseUrl() + "get_chats_for_channel/?pagenumber=" + nextPage + "&channel_id=" + channelId);
                if (entry != null) {
                    try {
                        String data = new String(entry.data, "UTF-8");
                        ZChatObject obj = new Gson().fromJson(data,
                                ZChatObject.class);
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
        });
        ZApplication.getInstance().addToRequestQueue(req, Z_CHATS);
    }

    private void setAdapterData(ZChatObject obj) {
        hideConnectionErrorLayout();
        hideProgressLayout();
        nextPage = obj.getNext_page();
        if (obj.getNext_page() == null)
            isMoreAllowed = false;
        else
            isMoreAllowed = true;
        if (adapter == null) {
            adapter = new ChatListAdapter(ChatActivity.this,
                    obj.getChats(), isMoreAllowed);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.addItemsToListAtEnd(obj.getChats(), isMoreAllowed);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retrylayoutconnectionerror:
                loadData();
                break;
            case R.id.sendbutton:
                if (messageBox.getText().toString().trim().length() > 0) {
                    sendMessageToServer();
                }
                break;
        }
    }
}
