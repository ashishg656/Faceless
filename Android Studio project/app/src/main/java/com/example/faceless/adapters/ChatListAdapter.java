package com.example.faceless.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.faceless.R;
import com.example.faceless.activities.ChatActivity;
import com.example.faceless.extras.AppConstants;
import com.example.faceless.extras.ChatTimeUtils;
import com.example.faceless.objects.ZChatObject;
import com.example.faceless.objects.ZChatObject.ChatItem;

import java.util.List;

public class ChatListAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

    Context context;
    List<ZChatObject.ChatItem> chats;
    boolean isMoreAllowed;

    public ChatListAdapter(Context context, List<ChatItem> chats,
                           boolean isMoreAllowed) {
        super();
        this.context = context;
        this.chats = chats;
        this.isMoreAllowed = isMoreAllowed;
    }

    @Override
    public int getItemCount() {
        return isMoreAllowed ? chats.size() + 1 : chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == chats.size()) {
            return Z_CHAT_TYPE_LOADING;
        }
        return Z_CHAT_TYPE_CHAT;
    }

    @Override
    public void onBindViewHolder(ViewHolder holdercom, int pos) {
        pos = holdercom.getAdapterPosition();
        if (getItemViewType(pos) == Z_CHAT_TYPE_CHAT) {
            ChatHolder holder = (ChatHolder) holdercom;
            holder.chatText.setText(chats.get(pos).getText());

            holder.chatTime.setText(ChatTimeUtils.getChatTime(chats.get(pos).getTime()));

            holder.chatDateHolder.setVisibility(View.GONE);

            try {
                ZChatObject.ChatItem chatCurrent = chats.get(pos);
                ZChatObject.ChatItem chatPrevious = chats.get(pos + 1);

                String currentTime = ChatTimeUtils.getSimpleDate(chatCurrent.getTime());
                String previousTime = ChatTimeUtils.getSimpleDate(chatPrevious.getTime());

                if (!currentTime.equals(previousTime)) {
                    holder.chatDateHolder.setVisibility(View.VISIBLE);
                    holder.chatDate.setText(ChatTimeUtils.getChatDateDisplayed(chatCurrent.getTime()));
                }
            } catch (Exception e) {
                if (pos + 1 == chats.size()) {
                    if (!((ChatActivity) context).isRequestRunning && !((ChatActivity) context).isMoreAllowed) {
                        Log.w("as", "last");
                        holder.chatDateHolder.setVisibility(View.VISIBLE);
                        holder.chatDate.setText(ChatTimeUtils.getChatDateDisplayed(chats.get(pos).getTime()));
                    }
                }
            }
        }
    }

    public void addItemToListAtBeggining(ZChatObject.ChatItem item) {
        chats.add(0, item);
        notifyDataSetChanged();
    }

    public void addItemsToListAtEnd(List<ChatItem> items, boolean isMoreAllowed) {
        this.isMoreAllowed = isMoreAllowed;
        chats.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        if (type == Z_CHAT_TYPE_LOADING) {
            View v = LayoutInflater.from(context).inflate(
                    R.layout.loading_more_layout, parent, false);
            ChatLoadingHolder holder = new ChatLoadingHolder(v);
            return holder;
        } else {
            View v = LayoutInflater.from(context).inflate(
                    R.layout.chat_list_item_layout, parent, false);
            ChatHolder holder = new ChatHolder(v);
            return holder;
        }
    }

    class ChatHolder extends RecyclerView.ViewHolder {

        TextView chatText, chatTime, chatDate;
        LinearLayout chatDateHolder;

        public ChatHolder(View v) {
            super(v);
            chatText = (TextView) v.findViewById(R.id.chattext);
            chatTime = (TextView) v.findViewById(R.id.chattime);
            chatDateHolder = (LinearLayout) v.findViewById(R.id.chatdatelayout);
            chatDate = (TextView) v.findViewById(R.id.dateforchat);
        }
    }

    class ChatLoadingHolder extends RecyclerView.ViewHolder {

        public ChatLoadingHolder(View itemView) {
            super(itemView);
        }
    }
}
