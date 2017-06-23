package com.yd.ychat.manager;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.yd.ychat.port.MessageList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by 荀高杰 on 2017/5/10.
 */

public class MessageManager {
    private MessageList messageList;
    private static MessageManager messageManager=new MessageManager();

    /**
     *
     * @return 返回一个消息管理器的对象
     */
    public static MessageManager getInstance (){
        return messageManager;
    }


    public MessageList getMessageList() {
        return messageList;
    }

    public void setMessageList(MessageList messageList) {
        this.messageList = messageList;
    }

    /**
     * 创建文本消息
     * @param txt 消息内容
     * @param name 接收人
     * @return 消息对象
     */
    public EMMessage creatTxtmsg(String txt,String name,EMMessage.ChatType type) {
        EMMessage txtSendMessage = EMMessage.createTxtSendMessage(txt, name);
        sendmsg(txtSendMessage,type);
        return txtSendMessage;
    }

    /**
     * 创建图片信息
     * @param path 图片路径
     * @param name 接收人
     * @param isoriginal 是否发送原图 true代表原图  反之false
     * @return 消息对象
     */
    public EMMessage creatImagemsg(String path,String name,boolean isoriginal,EMMessage.ChatType type) {
        EMMessage imageSendMessage = EMMessage.createImageSendMessage(path, isoriginal, name);
        sendmsg(imageSendMessage,type);
        return imageSendMessage;
    }

    /**
     * 创建语音消息
     * @param path 录音地址
     * @param time 录音时间
     * @param name 接收人
     * @return 消息对象
     */
    public EMMessage creatYuyin(String path, int time,String name,EMMessage.ChatType type) {
        EMMessage voiceSendMessage = EMMessage.createVoiceSendMessage(path, time, name);
        sendmsg(voiceSendMessage,type);
        return voiceSendMessage;
    }

    /**
     * 创建视屏消息
     * @param data 照相机返回的Intent
     * @param context 上下文
     * @param name 接收人
     * @return  消息对象
     */
    public EMMessage creatVideo(Intent data,Context context,String name,EMMessage.ChatType type) {
        String viodepath = getviodepath(data,context);
        File imagefile = getImagefile(viodepath,context);
        int videotime = getvideotime(viodepath);
        EMMessage videoSendMessage = EMMessage.createVideoSendMessage(viodepath, imagefile.getAbsolutePath(), videotime, name);
        sendmsg(videoSendMessage,type);
        return videoSendMessage;
    }

    /**
     * 得到视频的某一贞图像
     * @param viodepath 视频路径
     * @param context 上下文
     * @return 一个图片文件
     */
    private File getImagefile(String viodepath,Context context) {

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        //设置数据源
        mmr.setDataSource(viodepath);
        //选择要读取的图片
        Bitmap bitmap = mmr.getFrameAtTime(1000);
        String filename = System.currentTimeMillis() + ".jpg";
        File file = new File(context.getCacheDir()+"/"+filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            OutputStream os = new FileOutputStream(file);
            //写入手机
            bitmap.compress(Bitmap.CompressFormat.JPEG
                    , 50
                    , os
            );
            os.flush();
            os.close();
            os = null;
            bitmap.recycle();
            bitmap = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private int getvideotime(String viodepath) {
        int duration = 0;
        MediaPlayer mp = new MediaPlayer();
        try {
            //设置数据源
            mp.setDataSource(viodepath);
            //得到时间
            duration = mp.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.reset();
        mp.release();
        mp=null;
        return duration;
    }

    private String getviodepath(Intent data,Context context) {

        String videopath = null;
        //实例化一个内容解析器
        ContentResolver cr = context.getContentResolver();
        //查有关 MediaStore.Video.Media.DATA 的数据
        Cursor cursor = cr.query(data.getData()
                , new String[]{MediaStore.Video.Media.DATA}
                , null
                , null
                , null
        );
        if (cursor != null) {
            if(cursor.moveToNext()){
                //得到 MediaStore.Video.Media.DATA 字段的的内容
                videopath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            }

        }
        cursor.close();
        return videopath;
    }

    public void sendmsg(EMMessage msg,EMMessage.ChatType type) {
//        设置类型
        msg.setChatType(type);
//        发送消息
        EMClient.getInstance().chatManager().sendMessage(msg);
//        刷新会话列表
        MessageManager.getInstance().getMessageList().refreshChatList();


//        msg.setMessageStatusCallback(new EMCallBack() {
//            @Override
//            public void onSuccess() {
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//
//            @Override
//            public void onProgress(int i, String s) {
//
//            }
//        });


    }
}
