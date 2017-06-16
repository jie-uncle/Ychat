package com.yd.ychat.houdler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.yd.ychat.R;
import com.yd.ychat.act.Chat_Image_Activity;
import com.yd.ychat.port.Chat_msg_click;

/**
 * Created by 荀高杰 on 2017/5/24.
 */

public class Imageviewhoudler extends RecyclerView.ViewHolder  {
    public static final String URL = "url";
    private  View item_chat_right_iv_lay;
    private  View item_chat_left_iv_lay;
    private ImageView item_chat_msg_iv_right;
    private ImageView item_chat_msg_iv_left;
    private View image_lay;
    private Context context;

    public Imageviewhoudler(View itemView ) {
        super(itemView);
        initview(itemView);
    }
    private void initview(View v) {
        image_lay = v.findViewById(R.id.item_chat_message_image_lay);
        item_chat_right_iv_lay=v.findViewById(R.id.item_chat_right_iv_lay);
        item_chat_left_iv_lay= v.findViewById(R.id.item_chat_left_iv_lay);
        item_chat_msg_iv_right= (ImageView) v.findViewById(R.id.item_chat_msg_iv_right);
        item_chat_msg_iv_left= (ImageView) v.findViewById(R.id.item_chat_msg_iv_left);
    }
    public void setview(final Context context, final EMMessage m) {
        this.context=context;
        EMMessage.Type type = m.getType();
        final String emMessageFrom = m.getFrom();
        if(emMessageFrom.equals(EMClient.getInstance().getCurrentUser())){
            item_chat_left_iv_lay.setVisibility(View.GONE);
            item_chat_right_iv_lay.setVisibility(View.VISIBLE);
            if(type== EMMessage.Type.IMAGE){
                EMImageMessageBody body = (EMImageMessageBody) m.getBody();
                final String url = body.getLocalUrl();
                String thumbnailUrl = body.getThumbnailUrl();
                Glide.with(context).load(thumbnailUrl).into(item_chat_msg_iv_right);
                item_chat_msg_iv_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inten2chat_Image(url);
                    }
                });
            }
        }else {
            item_chat_right_iv_lay.setVisibility(View.GONE);
            item_chat_left_iv_lay.setVisibility(View.VISIBLE);
            if(type== EMMessage.Type.IMAGE){
                EMImageMessageBody body = (EMImageMessageBody) m.getBody();
                final String url = body.getThumbnailUrl();
                String thumbnailUrl = body.getThumbnailUrl();
                Glide.with(context).load(thumbnailUrl).into(item_chat_msg_iv_left);
                item_chat_msg_iv_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inten2chat_Image(url);
                    }
                });
            }
        }

    }
    private void inten2chat_Image(String url){
        Intent  i=new Intent(context, Chat_Image_Activity.class);
        i.putExtra(URL,url);
        context.startActivity(i);
    }
}
