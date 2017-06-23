package com.yd.ychat.houdler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVideoMessageBody;
import com.yd.ychat.R;
import com.yd.ychat.act.Chat_Image_Activity;
import com.yd.ychat.act.Chat_Video_Activity;

import java.io.File;
import java.util.HashMap;

/**
 * Created by 荀高杰 on 2017/5/24.
 */

public class Videohoudler extends RecyclerView.ViewHolder  {
    public static final String URL = "url";
    public static final String VIDEO = "video";
    private  View item_chat_message_video_right;
    private  View item_chat_message_video_left;
    private ImageView item_chat_message_video_image_right;
    private ImageView  item_chat_message_video_image_left;
    private TextView group_username;

    private Context context;

    public Videohoudler(View itemView ) {
        super(itemView);
        initview(itemView);
    }
    private void initview(View v) {

        item_chat_message_video_right=v.findViewById(R.id.item_chat_message_video_right);
        item_chat_message_video_left= v.findViewById(R.id.item_chat_message_video_left);
        item_chat_message_video_image_right= (ImageView) v.findViewById(R.id.item_chat_message_video_image_right);
        item_chat_message_video_image_left= (ImageView) v.findViewById(R.id. item_chat_message_video_image_left);
        group_username= (TextView) v.findViewById(R.id.group_username_txt);
    }
    public void setview(final Context context, final EMMessage m) {
        this.context = context;
        EMMessage.Type type = m.getType();
        final String emMessageFrom = m.getFrom();
        if (emMessageFrom.equals(EMClient.getInstance().getCurrentUser())) {
            item_chat_message_video_left.setVisibility(View.GONE);
            item_chat_message_video_right.setVisibility(View.VISIBLE);
            if (type == EMMessage.Type.VIDEO) {
                EMVideoMessageBody body = (EMVideoMessageBody) m.getBody();

                String localThumb = body.getLocalThumb();

                String url = body.getLocalUrl();
                if(new File(url).exists()){
                    Glide.with(context).load(localThumb).into(item_chat_message_video_image_right);
                    final String finalUrl = url;
                    item_chat_message_video_image_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intent2video(finalUrl);
                        }
                    });
                }else{
                    item_chat_message_video_image_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           Toast.makeText(context,"文件损坏",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        } else {
            if(m.getChatType()== EMMessage.ChatType.GroupChat){
                group_username.setVisibility(View.VISIBLE);
                group_username.setText(m.getFrom());
            }else{
                group_username.setVisibility(View.GONE);
            }
            item_chat_message_video_right.setVisibility(View.GONE);
            item_chat_message_video_left.setVisibility(View.VISIBLE);
            if (type == EMMessage.Type.VIDEO) {
                final EMVideoMessageBody body = (EMVideoMessageBody) m.getBody();
                    Glide.with(context).load(body.getThumbnailUrl()).into(item_chat_message_video_image_left);
                final String path = body.getLocalUrl();
                if(new File(path).exists()){
                    item_chat_message_video_image_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intent2video(path);
                        }
                    });


                }else{
                    Toast.makeText(context,"正在加载。。加载完成后再次点击",Toast.LENGTH_SHORT).show();
                    final String locpath=context.getCacheDir()+"/"+System.currentTimeMillis()+".mp4";
                    HashMap<String,String> map=new HashMap<String, String>();
                    if (!TextUtils.isEmpty(body.getSecret())){
                        map.put("share-secret",body.getSecret());
                    }
                     EMClient.getInstance().chatManager().downloadFile(
                             body.getRemoteUrl()
                             , locpath
                             , map
                             , new EMCallBack() {
                                 @Override
                                 public void onSuccess() {
                                     //修改消息的本地路径
                                     ((EMVideoMessageBody) m.getBody()).setLocalUrl(locpath);
                                     //更新数据库消息
                                     EMClient.getInstance().chatManager().updateMessage(m);
                                     Toast.makeText(context,"加载完成",Toast.LENGTH_SHORT).show();
                                 }

                                 @Override
                                 public void onError(int i, String s) {

                                 }

                                 @Override
                                 public void onProgress(int i, String s) {

                                 }
                             }
                     );
                }





            }
        }

    }
    public void intent2video(String url){
        Intent i=new Intent(context, Chat_Video_Activity.class);
        i.putExtra(VIDEO,url);
        context.startActivity(i);
    }
}
