package com.yd.ychat.fragment;

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

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by 荀高杰 on 2017/5/28.
 */

public class YuyinFragmrnt extends BaseFragment {
    private ImageView message_fragment_yuyin_iv_on, message_fragment_yuyin_iv_down;
    private View message_fragment_yuyin_iv_lay;
    private ChatActivity chatActivity;
    private TextView message_fragment_yuyin_tv;
    private Timer timer;
    private TimerTask timerTask;
    private MediaRecorder mr = null;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String times= (String) msg.obj;
            message_fragment_yuyin_tv.setText(times);
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
        message_fragment_yuyin_iv_lay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String path = null;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        path=Environment.getExternalStorageDirectory()+"/"+System.currentTimeMillis()+".amr";
                        message_fragment_yuyin_iv_on.setVisibility(View.GONE);
                        message_fragment_yuyin_iv_down.setVisibility(View.VISIBLE);
//                        if(mr==null){
//                            mr=new MediaRecorder();
//                        }else{
//                            mr.release();
//                         }
//                        ready_record(path);
//                        mr.start();
                        settime();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
//                        if (mr != null) {
//                            mr.stop();
//                            mr.release();
//                            mr.reset();
//
//                            mr=null;
                            timer.cancel();
                            timerTask.cancel();
                            timer=null;
                            timerTask=null;
//                        }
                        message_fragment_yuyin_iv_on.setVisibility(View.VISIBLE);
                        message_fragment_yuyin_iv_down.setVisibility(View.GONE);
                        if(time<=1){
                            Toast.makeText(getContext(),"录音时间太短",Toast.LENGTH_SHORT);
                        }
//                        chatActivity.creatYuyin(path,time);
                        message_fragment_yuyin_tv.setText("按住说话");
                        break;
                }
                return true;
            }
        });
    }

    private void initview(View view) {
        message_fragment_yuyin_iv_on = (ImageView) view.findViewById(R.id.message_fragment_yuyin_iv_on);
        message_fragment_yuyin_iv_down = (ImageView) view.findViewById(R.id.message_fragment_yuyin_iv_down);
        message_fragment_yuyin_iv_lay = view.findViewById(R.id.message_fragment_yuyin_iv_lay);
        message_fragment_yuyin_tv = (TextView) view.findViewById(R.id.message_fragment_yuyin_tv);
        chatActivity = (ChatActivity) getActivity();
    }

    private void ready_record(String path) {
        try {
         mr = new MediaRecorder();
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mr.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mr.setOutputFile(path);
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mr.prepare();
        } catch (IllegalStateException e) {

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public int time;
    private void settime(){
        time=0;
                timer=new Timer();
                timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        String times = timeutil(time);
                        Message msg = handler.obtainMessage();
                        msg.obj=times;
                        msg.what=0;
                        handler.sendMessage(msg);
                        time++;
                    }
                };
                timer.schedule(timerTask,0,1000);
            }



    private String timeutil(int time){
        String times;
        if(time>60){
            int minute = time / 60;
            int second = time % 60;
            if(second<10){
                times=minute+":0"+second;
            }else{
                times=minute+":"+second;
            }
        }else {
            if(time<10){
                times="0:0"+time;
            }else{
                times="0:"+time;
            }

        }
        return times;
    }
}
