package com.example.faceless.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.faceless.R;
import com.example.faceless.activities.ChatActivity;
import com.example.faceless.objects.ZAllChannelsObject;

/**
 * Created by praveen goel on 10/16/2015.
 */
public class ChannelListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ZAllChannelsObject mData;
    MyClickListener clickListener;

    public ChannelListAdapter(Context context, ZAllChannelsObject mData) {
        this.context = context;
        this.mData = mData;
        clickListener = new MyClickListener();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.channel_list_item_layouts, parent, false);
        ChannelListHolder holder = new ChannelListHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderC, int position) {
        position = holderC.getAdapterPosition();

        ChannelListHolder holder = (ChannelListHolder) holderC;
        holder.containerLayout.setTag(position);
        holder.containerLayout.setOnClickListener(clickListener);

        holder.channelName.setText(mData.getChannels().get(position).getName());
        if (mData.getChannels().get(position).is_unsubscribed()) {
            holder.lastMessage.setText("Unsubscribed from this channel");
            holder.lastMessageTime.setVisibility(View.GONE);
            holder.isUnsubscribed.setVisibility(View.VISIBLE);
        } else {
            holder.lastMessageTime.setVisibility(View.VISIBLE);
            holder.isUnsubscribed.setVisibility(View.GONE);

            if (mData.getChannels().get(position).getLast_chat_time() != null) {
                holder.lastMessageTime.setText(mData.getChannels().get(position).getLast_chat_time());
            } else {
                holder.lastMessageTime.setText("");
            }

            if (mData.getChannels().get(position).getLast_chat_msg() != null) {
                holder.lastMessage.setText(mData.getChannels().get(position).getLast_chat_msg());
            } else {
                holder.lastMessage.setText("No messages.");
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.getChannels().size();
    }

    class ChannelListHolder extends RecyclerView.ViewHolder {

        FrameLayout containerLayout;
        TextView channelName, lastMessage, lastMessageTime, isUnsubscribed;

        public ChannelListHolder(View v) {
            super(v);
            containerLayout = (FrameLayout) v.findViewById(R.id.channellistholder);
            channelName = (TextView) v.findViewById(R.id.channelname);
            lastMessage = (TextView) v.findViewById(R.id.lastmessage);
            lastMessageTime = (TextView) v.findViewById(R.id.channledltime);
            isUnsubscribed = (TextView) v.findViewById(R.id.unsubscrbed);
        }
    }

    class MyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.channellistholder) {
                Intent i = new Intent(context,
                        ChatActivity.class);
                int pos = (int) v.getTag();
                i.putExtra("channel_id",
                        mData.getChannels().get(pos).getId());
                i.putExtra("channel_name",
                        mData.getChannels().get(pos).getName());
                context.startActivity(i);
            }
        }
    }
}
