package com.yd.ychat.houdler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.yd.ychat.R;
import com.yd.ychat.port.Chat_msg_click;

/**
 * Created by 荀高杰 on 2017/5/24.
 */

public class Imageviewhoudler extends RecyclerView.ViewHolder  {
    private  View item_chat_right_iv_lay;
    private  View item_chat_left_iv_lay;
    private ImageView item_chat_msg_iv_right;
    private ImageView item_chat_msg_iv_left;
    private View image_lay;
    private Chat_msg_click click;

    public void setClick(Chat_msg_click click) {
        this.click = click;
    }

    public Imageviewhoudler(View itemView ) {
        super(itemView);
        initview(itemView);
        setClick();
    }

    private void setClick() {
        image_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click!=null){
                    click.itemclick();
                }
            }
        });
        image_lay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(click!=null){
                    click.itemlongclick();
                }
                return false;
            }
        });
    }

    private void initview(View v) {
        image_lay = v.findViewById(R.id.item_chat_message_image_lay);
        item_chat_right_iv_lay=v.findViewById(R.id.item_chat_right_iv_lay);
        item_chat_left_iv_lay= v.findViewById(R.id.item_chat_left_iv_lay);
        item_chat_msg_iv_right= (ImageView) v.findViewById(R.id.item_chat_msg_iv_right);
        item_chat_msg_iv_left= (ImageView) v.findViewById(R.id.item_chat_msg_iv_left);
    }
    public void setview(Context context, EMMessage m) {
        EMMessage.Type type = m.getType();
        String emMessageFrom = m.getFrom();
        if(emMessageFrom.equals(EMClient.getInstance().getCurrentUser())){
            item_chat_left_iv_lay.setVisibility(View.GONE);
            item_chat_right_iv_lay.setVisibility(View.VISIBLE);
            if(type== EMMessage.Type.IMAGE){
                EMImageMessageBody body = (EMImageMessageBody) m.getBody();
                String url = body.getLocalUrl();
                Glide.with(context).load(url).into(item_chat_msg_iv_right);
            }
        }else {
            item_chat_right_iv_lay.setVisibility(View.GONE);
            item_chat_left_iv_lay.setVisibility(View.VISIBLE);
            if(type== EMMessage.Type.IMAGE){
                EMImageMessageBody body = (EMImageMessageBody) m.getBody();
                String url = body.getThumbnailUrl();
                Glide.with(context).load(url).into(item_chat_msg_iv_left);
            }
        }
    }
}
