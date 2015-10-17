package com.example.faceless.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.faceless.R;
import com.example.faceless.application.ZApplication;
import com.example.faceless.extras.AppConstants;
import com.example.faceless.objects.ZPostObject;
import com.example.faceless.serverApi.ImageRequestManager;

import java.util.List;

/**
 * Created by praveen goel on 10/17/2015.
 */
public class PostListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AppConstants {

    List<ZPostObject> mData;
    boolean isMoreAllowed;
    Context context;

    public PostListAdapter(List<ZPostObject> mData, boolean isMoreAllowed, Context context) {
        this.mData = mData;
        this.isMoreAllowed = isMoreAllowed;
        this.context = context;
    }

    public void addItemsToListAtEnd(List<ZPostObject> items, boolean isMoreAllowed) {
        this.isMoreAllowed = isMoreAllowed;
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        if (type == Z_CHAT_TYPE_LOADING) {
            View v = LayoutInflater.from(context).inflate(
                    R.layout.loading_more_layout, parent, false);
            ChatLoadingHolder holder = new ChatLoadingHolder(v);
            return holder;
        } else {
            View v = LayoutInflater.from(context).inflate(
                    R.layout.post_item_layout, parent, false);
            ChatHolder holder = new ChatHolder(v);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holdercom, int pos) {
        pos = holdercom.getAdapterPosition();
        if (getItemViewType(pos) == Z_CHAT_TYPE_CHAT) {
            ChatHolder holder = (ChatHolder) holdercom;
            ZPostObject obj = mData.get(pos);

            if (obj.is_poll()) {
                holder.pollLayout.setVisibility(View.VISIBLE);
            } else
                holder.pollLayout.setVisibility(View.GONE);

            holder.heading.setText(obj.getHeading());
            holder.description.setText(obj.getDescription());
            holder.numberofUpvotes.setText(obj.getUpvotes_count() + "");
            holder.comments.setText(obj.getComment_count() + " comments");
            holder.time.setText(obj.getDate());

            if (obj.getImage() == null) {
                holder.image.setVisibility(View.GONE);
            } else {
                holder.image.setVisibility(View.VISIBLE);
                ImageRequestManager.get(context).requestImage(context, holder.image, ZApplication.getInstance().getImageUrl(obj.getImage()), -4);
            }
        }
    }

    @Override
    public int getItemCount() {
        return isMoreAllowed ? mData.size() + 1 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mData.size()) {
            return Z_CHAT_TYPE_LOADING;
        }
        return Z_CHAT_TYPE_CHAT;
    }

    class ChatHolder extends RecyclerView.ViewHolder {

        LinearLayout pollLayout;
        TextView heading, description, time, comments, numberofUpvotes;
        ImageView image, upvotes, downvotes, favourite;

        public ChatHolder(View v) {
            super(v);
            pollLayout = (LinearLayout) v.findViewById(R.id.polllayout);
            heading = (TextView) v.findViewById(R.id.pollheading);
            time = (TextView) v.findViewById(R.id.posttime);
            description = (TextView) v.findViewById(R.id.desc);
            image = (ImageView) v.findViewById(R.id.image);
            comments = (TextView) v.findViewById(R.id.comemnsts);
            upvotes = (ImageView) v.findViewById(R.id.upvo);
            downvotes = (ImageView) v.findViewById(R.id.downvotes);
            numberofUpvotes = (TextView) v.findViewById(R.id.numupvotes);
            favourite = (ImageView) v.findViewById(R.id.like);
        }
    }

    class ChatLoadingHolder extends RecyclerView.ViewHolder {

        public ChatLoadingHolder(View itemView) {
            super(itemView);
        }
    }
}
