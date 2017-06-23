package com.yd.ychat.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yd.ychat.R;
import com.yd.ychat.act.ChatActivity;
import com.yd.ychat.act.GroupChatActivity;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by 荀高杰 on 2017/5/28.
 */

public class YuyinFragmrnt extends BaseFragment {
    private ImageView message_fragment_yuyin_iv_on, message_fragment_yuyin_iv_down;
    private View message_fragment_yuyin_iv_lay;

    private TextView message_fragment_yuyin_tv;
    private Timer timer;
    private TimerTask timerTask;
    private MediaRecorder mr;
    String path = null;
    public int time;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time++;
            message_fragment_yuyin_tv.setText(timeutil(time));
            handler.sendEmptyMessageDelayed(1,1000);
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.message_fragment_yuyin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);
        setTouch();
    }

    private void setTouch() {
        message_fragment_yuyin_iv_lay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (ishave()==true) {
                                path = getContext().getCacheDir().getPath() + "/" + System.currentTimeMillis() + ".amr";
                                message_fragment_yuyin_iv_on.setVisibility(View.GONE);
                                message_fragment_yuyin_iv_down.setVisibility(View.VISIBLE);
                                startMr(path);
                                message_fragment_yuyin_tv.setText(timeutil(time));
                                handler.sendEmptyMessageDelayed(1,1000);

                        } else {
                            Toast.makeText(getContext(), "请设置给该程序录音权限", Toast.LENGTH_LONG).show();
                        }

                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeMessages(1);
                        closeMr();
                        message_fragment_yuyin_iv_on.setVisibility(View.VISIBLE);
                        message_fragment_yuyin_iv_down.setVisibility(View.GONE);
                        if (time > 0) {
                            if (path != null) {
                                if(getActivity() instanceof ChatActivity){
                                   ((ChatActivity) getActivity()).creatYuyin(path,time);
                                }else if(getActivity() instanceof GroupChatActivity){
                                    ((GroupChatActivity) getActivity()).creatYuyin(path,time);
                                }
                            }
                        } else {
                            new File(path).delete();
                            Toast.makeText(getContext(), "录音时间太短", Toast.LENGTH_SHORT).show();
                        }
                        message_fragment_yuyin_tv.setText("按住说话");
                        path = "";
                        time=0;
                        break;
                }
                return true;
            }


        });
    }

    private boolean ishave() {
        PackageManager packageManager = getContext().getPackageManager();
        return PackageManager.PERMISSION_GRANTED == packageManager.checkPermission("android.permission.RECORD_AUDIO", "com.yd.ychat");
    }

    private void initview(View view) {
        message_fragment_yuyin_iv_on = (ImageView) view.findViewById(R.id.message_fragment_yuyin_iv_on);
        message_fragment_yuyin_iv_down = (ImageView) view.findViewById(R.id.message_fragment_yuyin_iv_down);
        message_fragment_yuyin_iv_lay = view.findViewById(R.id.message_fragment_yuyin_iv_lay);
        message_fragment_yuyin_tv = (TextView) view.findViewById(R.id.message_fragment_yuyin_tv);

    }

    private void startMr(String path) {
        if(mr==null){
            mr = new MediaRecorder();
        }
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mr.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mr.setOutputFile(path);
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mr.prepare();
            mr.start();
        } catch (IllegalStateException e) {

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public static String timeutil(int time) {
        String times;
        if (time >= 60) {
            int minute = time / 60;
            int second = time % 60;
            if (second < 10) {
                times = minute + ":0" + second;
            } else {
                times = minute + ":" + second;
            }
        } else {
            if (time < 10) {
                times = "0:0" + time;
            } else {
                times = "0:" + time;
            }
        }
        return times;
    }
    private void closeMr() {
        if(mr!=null){
            mr.stop();
            mr.reset();
            mr.release();
        }

    }
}
