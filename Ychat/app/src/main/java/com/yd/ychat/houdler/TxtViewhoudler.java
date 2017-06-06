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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 荀高杰 on 2017/5/24.
 */

public class TxtViewhoudler extends RecyclerView.ViewHolder {
    private  View item_chat_msg_right_lay;
    private  View item_chat_msg_left_lay;
    private Map<String,Integer> map=Face_List.getInstanceMap();
    private View txt_lay;

    private Context context;
    private TextView item_chat_msg_tv_left;
    private TextView item_chat_msg_tv_right;
    private Chat_msg_click click;

    public void setClick(Chat_msg_click click) {
        this.click = click;
    }

    public TxtViewhoudler(View itemView, Context context) {
        super(itemView);
        initview(itemView);
        this.context=context;
        setClick();
    }

    private void setClick() {
        txt_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(click!=null){
                    click.itemclick();
                }
            }
        });
        txt_lay.setOnLongClickListener(new View.OnLongClickListener() {
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
        txt_lay = v.findViewById(R.id.item_chat_message_txt_lay);
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
                SpannableStringBuilder newmsg = handler(msg);
                item_chat_msg_tv_right.setText(newmsg);
            }
        }else{

            item_chat_msg_left_lay.setVisibility(View.VISIBLE);
            item_chat_msg_right_lay.setVisibility(View.GONE);
            if(type== EMMessage.Type.TXT){
                EMTextMessageBody emtxtmsg = (EMTextMessageBody) message.getBody();
                String msg = emtxtmsg.getMessage();
                SpannableStringBuilder newmsg = handler(msg);
                item_chat_msg_tv_left.setText(newmsg);
            }

        }
    }
    private SpannableStringBuilder handler( String content) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "(\\[)\\w{3}(\\])";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String tempText = m.group();



            Drawable d = context.getResources().getDrawable(map.get(tempText));
            d.setBounds(0, 0, d.getIntrinsicWidth()/2,  d.getIntrinsicHeight()/2);
            //用这个drawable对象代替字符串easy
            ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);

                sb.setSpan(span, m.start(), m.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        return sb;
    }
}
