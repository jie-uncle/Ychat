package com.yd.ychat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.yd.ychat.R;
import com.yd.ychat.houdler.Imageviewhoudler;
import com.yd.ychat.houdler.TxtViewhoudler;

import java.util.List;

/**
 * Created by 荀高杰 on 2017/5/9.
 */

public class ChatRecyclerAdapter extends RecyclerView.Adapter {
    public static final int TXT = 0;
    public static final int IMAGE = 1;
    public static final int VIDEO = 2;
    private List<EMMessage> messages;
    private String name;
    private Context context;

    public ChatRecyclerAdapter(List<EMMessage> messages, Context context, String name) {
        this.messages = messages;
        this.context = context;
        this.name = name;
    }

    public void refresh(List<EMMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TXT:
                return new TxtViewhoudler(LayoutInflater.from(context).inflate(R.layout.item_chat_massage_txt, parent, false));
            case IMAGE:
                return new Imageviewhoudler(LayoutInflater.from(context).inflate(R.layout.item_chat_massage_image, parent, false));
            case VIDEO:

        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = messages.get(position);
        EMMessage.Type type = emMessage.getType();
        switch (type) {
            case TXT:
                return  this.TXT;
            case IMAGE:
                return this.IMAGE;
            case VIDEO:
                return this.VIDEO;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        EMMessage emMessage = messages.get(position);


       if(holder instanceof TxtViewhoudler){
           ((TxtViewhoudler)holder).setview(emMessage);
       }else if(holder instanceof Imageviewhoudler){
           ((Imageviewhoudler)holder).setview(context,emMessage);
       }

    }


    @Override
    public int getItemCount() {
        return messages.size();
    }
}
