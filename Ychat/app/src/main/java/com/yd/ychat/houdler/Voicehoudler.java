package com.yd.ychat.houdler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.yd.ychat.R;
import com.yd.ychat.port.Chat_msg_click;
import com.yd.ychat.utils.Play_voice;

/**
 * Created by 荀高杰 on 2017/5/28.
 */

public class Voicehoudler extends RecyclerView.ViewHolder {
    private View item_chat_msg_yuyin_left;
    private TextView item_chat_msg_yuyin_left_time;
    private View item_chat_msg_yuyin_right;
    private TextView item_chat_msg_yuyin_right_time;
    private View yuyin_lay;
    private Chat_msg_click click;

    public void setClick(Chat_msg_click click) {
        this.click = click;
    }

    public Voicehoudler(View itemView) {
        super(itemView);
        initview(itemView);


    }



    private void initview(View v) {
         yuyin_lay = v.findViewById(R.id.item_chat_message_yuyin_lay);
         item_chat_msg_yuyin_left = v.findViewById(R.id.item_chat_msg_yuyin_left);
         item_chat_msg_yuyin_left_time = (TextView) v.findViewById(R.id.item_chat_msg_yuyin_left_time);
         item_chat_msg_yuyin_right = v.findViewById(R.id.item_chat_msg_yuyin_right);
         item_chat_msg_yuyin_right_time = (TextView) v.findViewById(R.id.item_chat_msg_yuyin_right_time);
    }
    public void setview(final EMMessage message){
        EMMessage.Type type = message.getType();
        final String emMessageFrom = message.getFrom();
        if(emMessageFrom.equals(EMClient.getInstance().getCurrentUser())){
            item_chat_msg_yuyin_left.setVisibility(View.GONE);
            item_chat_msg_yuyin_right.setVisibility(View.VISIBLE);
            if(type== EMMessage.Type.VOICE){
                EMVoiceMessageBody body = (EMVoiceMessageBody) message.getBody();
                int length = body.getLength();
                item_chat_msg_yuyin_right_time.setText(length+"");
            }
        }else{
            item_chat_msg_yuyin_left.setVisibility(View.VISIBLE);
            item_chat_msg_yuyin_right.setVisibility(View.GONE);
            if(type== EMMessage.Type.VOICE){
                EMVoiceMessageBody body = (EMVoiceMessageBody) message.getBody();
                int length = body.getLength();
                item_chat_msg_yuyin_left_time.setText(length+"");
            }
        }
        yuyin_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMVoiceMessageBody body = (EMVoiceMessageBody) message.getBody();
                if(emMessageFrom.equals(EMClient.getInstance().getCurrentUser())){
                    String url = body.getLocalUrl();
                    Play_voice.play(url);
                }else{
                    String url = body.getRemoteUrl();
                    Play_voice.play(url);
                }
            }
        });
    }
}
