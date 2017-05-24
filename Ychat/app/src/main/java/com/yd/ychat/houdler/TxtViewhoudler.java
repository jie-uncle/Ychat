package com.yd.ychat.houdler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.yd.ychat.R;

/**
 * Created by 荀高杰 on 2017/5/24.
 */

public class TxtViewhoudler extends RecyclerView.ViewHolder {
    private  View item_chat_msg_right_lay;
    private  View item_chat_msg_left_lay;


    private TextView item_chat_msg_tv_left;
    private TextView item_chat_msg_tv_right;
    public TxtViewhoudler(View itemView) {
        super(itemView);
        initview(itemView);
    }

    private void initview(View v) {
        item_chat_msg_right_lay = v.findViewById(R.id.item_chat_tv_right_lay);
        item_chat_msg_left_lay = v.findViewById(R.id.item_chat_tv_left_lay);
        item_chat_msg_tv_left= (TextView) v.findViewById(R.id.item_chat_msg_tv_left);
        item_chat_msg_tv_right= (TextView) v.findViewById(R.id.item_chat_msg_tv_right);
    }
    public void setview(EMMessage message){

        EMMessage.Type type = message.getType();

        String emMessageFrom = message.getFrom();
        if(emMessageFrom.equals(EMClient.getInstance().getCurrentUser())){
            item_chat_msg_left_lay.setVisibility(View.GONE);
            item_chat_msg_right_lay.setVisibility(View.VISIBLE);
            if(type== EMMessage.Type.TXT){
                EMTextMessageBody emtxtmsg = (EMTextMessageBody) message.getBody();
                String msg = emtxtmsg.getMessage();
                item_chat_msg_tv_right.setText(msg);
            }
        }else{

            item_chat_msg_left_lay.setVisibility(View.VISIBLE);
            item_chat_msg_right_lay.setVisibility(View.GONE);
            if(type== EMMessage.Type.TXT){
                EMTextMessageBody emtxtmsg = (EMTextMessageBody) message.getBody();
                String msg = emtxtmsg.getMessage();
                item_chat_msg_tv_left.setText(msg);
            }

        }
    }
}
