package com.yd.ychat.houdler;

import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.yd.ychat.R;
import com.yd.ychat.act.ChatActivity;

import java.io.IOException;

/**
 * Created by 荀高杰 on 2017/5/28.
 */

public class Voicehoudler extends RecyclerView.ViewHolder {
    private View item_chat_msg_yuyin_left,item_chat_msg_yuyin_left_yuyin,item_chat_msg_yuyin_right_yuyin;
    private TextView item_chat_msg_yuyin_left_time;
    private View item_chat_msg_yuyin_right;
    private TextView item_chat_msg_yuyin_right_time;
    private View yuyin_lay;
    private AlphaAnimation alphaAnimation;
    private View v=null;

    public Voicehoudler(View itemView) {
        super(itemView);
        initview(itemView);
    }
    private void initview(View v) {
         yuyin_lay = v.findViewById(R.id.item_chat_message_yuyin_lay);
         item_chat_msg_yuyin_left_yuyin = v.findViewById(R.id.item_chat_msg_yuyin_left_yuyin);
         item_chat_msg_yuyin_right_yuyin = v.findViewById(R.id.item_chat_msg_yuyin_right_yuyin);
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
                String time = time(length);
                final String url = body.getLocalUrl();
                item_chat_msg_yuyin_right_time.setText(time);
                item_chat_msg_yuyin_right_yuyin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        play(url,item_chat_msg_yuyin_right_yuyin);
                    }
                });
            }
        }else{
            item_chat_msg_yuyin_left.setVisibility(View.VISIBLE);
            item_chat_msg_yuyin_right.setVisibility(View.GONE);
            if(type== EMMessage.Type.VOICE){
                final EMVoiceMessageBody body = (EMVoiceMessageBody) message.getBody();
                int length = body.getLength();
                String time = time(length);
                final String url = body.getRemoteUrl();
                item_chat_msg_yuyin_left_time.setText(time);
                item_chat_msg_yuyin_left_yuyin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        play(url,item_chat_msg_yuyin_left_yuyin);
                    }
                });
            }
        }


    }
    private void animation_start(View view){
        ChatActivity.getRecycleview().clearAnimation();
        //开始动画
        animation();
        view.startAnimation(alphaAnimation);

    }


    private  MediaPlayer my ;
    private View aa;
    public  void play(String url, final View view) {

        if(my!=null&&my.isPlaying()){
            if(aa==view){
                my.stop();
                animation_close(view);
                return;
            }else{
                animation_close(aa);
            }

        }

        aa=view;
        if(my==null){
            my= new MediaPlayer();
        }
        my.reset();
        try {
            my.setDataSource(url);

            my.prepareAsync();
            my.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    my.start();
                    animation_start(view);
                }
            });
            my.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                   animation_close(view);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String time(int time){
        String times;
        if(time>=60){
            int minute = time / 60;
            int second = time % 60;
            times=minute+"'"+second+"\"";
        }else{
            times=time+"\"";
        }
        return times;
    }































    private void animation() {
        alphaAnimation = new AlphaAnimation(1.0f, 0.2f);
        //设置动画持续时长
        alphaAnimation.setDuration(100);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        alphaAnimation.setFillAfter(true);
        //设置动画结束之后的状态是否是动画开始时的状态，true，表示是保持动画开始时的状态
        alphaAnimation.setFillBefore(true);
        //设置动画的重复模式：反转REVERSE和重新开始RESTART
        alphaAnimation.setRepeatMode(AlphaAnimation.REVERSE);
        //设置动画播放次数
        alphaAnimation.setRepeatCount(AlphaAnimation.INFINITE);
    }

    private void animation_close(View view){
        view.clearAnimation();
        alphaAnimation.cancel();
    }
}
