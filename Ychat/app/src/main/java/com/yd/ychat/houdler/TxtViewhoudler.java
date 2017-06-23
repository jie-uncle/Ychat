package com.yd.ychat.houdler;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.yd.ychat.R;
import com.yd.ychat.array.Face_List;
import com.yd.ychat.port.Chat_msg_click;
import com.yd.ychat.utils.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 荀高杰 on 2017/5/24.
 */

public class TxtViewhoudler extends RecyclerView.ViewHolder {
    private View item_chat_msg_right_lay;
    private View item_chat_msg_left_lay;
    private Map<String, Integer> map = Face_List.getInstanceMap();
    private View txt_lay;


    private TextView item_chat_msg_tv_left;
    private TextView item_chat_msg_tv_right,group_username;


    public TxtViewhoudler(View itemView) {
        super(itemView);
        initview(itemView);


    }


    private void initview(View v) {
        txt_lay = v.findViewById(R.id.item_chat_message_txt_lay);
        item_chat_msg_right_lay = v.findViewById(R.id.item_chat_tv_right_lay);
        item_chat_msg_left_lay = v.findViewById(R.id.item_chat_tv_left_lay);
        item_chat_msg_tv_left = (TextView) v.findViewById(R.id.item_chat_msg_tv_left);
        item_chat_msg_tv_right = (TextView) v.findViewById(R.id.item_chat_msg_tv_right);
        group_username= (TextView) v.findViewById(R.id.group_username_txt);
    }

    public void setview(EMMessage message,Context context) {
        EMMessage.ChatType chatType = message.getChatType();
        EMMessage.Type type = message.getType();

        String emMessageFrom = message.getFrom();
        if (emMessageFrom.equals(EMClient.getInstance().getCurrentUser())) {
            item_chat_msg_left_lay.setVisibility(View.GONE);
            item_chat_msg_right_lay.setVisibility(View.VISIBLE);
            if (type == EMMessage.Type.TXT) {
                EMTextMessageBody emtxtmsg = (EMTextMessageBody) message.getBody();
                String msg = emtxtmsg.getMessage();
                SpannableStringBuilder newmsg = StringUtil.handler(context,msg);
                item_chat_msg_tv_right.setText(newmsg);
            }
        } else {
            if(chatType== EMMessage.ChatType.GroupChat){
                group_username.setVisibility(View.VISIBLE);
                group_username.setText(message.getFrom());
            }else{
                group_username.setVisibility(View.GONE);
            }

            item_chat_msg_left_lay.setVisibility(View.VISIBLE);
            item_chat_msg_right_lay.setVisibility(View.GONE);
            if (type == EMMessage.Type.TXT) {
                EMTextMessageBody emtxtmsg = (EMTextMessageBody) message.getBody();
                String msg = emtxtmsg.getMessage();
                SpannableStringBuilder newmsg = StringUtil.handler(context,msg);
                item_chat_msg_tv_left.setText(newmsg);
            }

        }
    }


}
