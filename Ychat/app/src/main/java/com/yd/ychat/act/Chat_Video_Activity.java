package com.yd.ychat.act;

import android.content.Intent;
import java.io.IOException;

import java.util.Timer;
import java.util.TimerTask;




import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.yd.ychat.R;
import com.yd.ychat.houdler.Videohoudler;

public class Chat_Video_Activity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private LinearLayout ll;
    private ImageView iv_stop;
    private ImageView iv_play;
    private SeekBar sb;
    private SurfaceView sv;
    private SharedPreferences sp;
    private MediaPlayer mp;
    private Timer timer;
    private TimerTask task;
    private String videopath;
    private View video_play_again;
    private boolean isplaying=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__video);
         videopath = getIntent().getStringExtra(Videohoudler.VIDEO);
        initview();
        initlistener();

    }






    public void initview() {

        sv = (SurfaceView) findViewById(R.id.sv);
        sb = (SeekBar) findViewById(R.id.seekbar);
        iv_play = (ImageView) findViewById(R.id.play);
        iv_stop = (ImageView) findViewById(R.id.stop);
        ll = (LinearLayout) findViewById(R.id.ll);
         video_play_again = findViewById(R.id.video_play_again);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        Intent i = getIntent();

    }


    public void initlistener() {
        iv_play.setOnClickListener(this);
        iv_stop.setOnClickListener(this);
        sv.getHolder().addCallback(new MyCallback());
        sb.setOnSeekBarChangeListener(this);
        video_play_again.setOnClickListener(this);
    }





    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            try {
                isplay();
                sb.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    public void run() {
                        SystemClock.sleep(5000);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                sb.setVisibility(View.GONE);
                                iv_play.setVisibility(View.GONE);
                                iv_stop.setVisibility(View.GONE);
                            }
                        });
                    }
                }

                ).start();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return super.onTouchEvent(event);
    }

    private void isplay() {
        if (mp.isPlaying()) {
            iv_stop.setVisibility(View.VISIBLE);

            iv_play.setVisibility(View.GONE);
        } else {
            iv_play.setVisibility(View.VISIBLE);
            iv_stop.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

            int currentPosition = mp.getCurrentPosition();
            switch (v.getId()) {
                case R.id.play:
                    iv_play.setVisibility(View.GONE);
                    iv_stop.setVisibility(View.VISIBLE);
                    if(isplaying){
                        mp.seekTo(currentPosition);
                    }

                    mp.start();
                    video_play_again.setVisibility(View.GONE);
                    break;

                case R.id.stop:
                    iv_stop.setVisibility(View.GONE);
                    iv_play.setVisibility(View.VISIBLE);
                    mp.pause();
                    break;
                case R.id.video_play_again:
                    mp.seekTo(0);
                    mp.start();
                    video_play_again.setVisibility(View.GONE);
                    break;

        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        mp.seekTo(progress);
        mp.start();
    }


    class MyCallback implements Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mp = new MediaPlayer();
                mp.setDisplay(sv.getHolder());
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.setDataSource(videopath);
                mp.prepareAsync();
                mp.setOnPreparedListener(new MyOnPreparedListener());
                mp.setOnCompletionListener(new MyOnCompletionListener());
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Editor edit = sp.edit();

            if(mp!=null&&mp.isPlaying()){
                int position=mp.getCurrentPosition();
                edit.putInt("position", position);
                edit.commit();
            }else{
                edit.putInt("position", 0);
                edit.commit();
            }
            mp.stop();
            if(timer!=null){
                timer.cancel();
                timer=null;
            }
            if(task!=null){
                task.cancel();
                task=null;
            }



            mp.release();
            mp=null;

        }

    }

    class MyOnPreparedListener implements OnPreparedListener {

        @Override
        public void onPrepared(final MediaPlayer mp) {

            int xiabiao = sp.getInt("position", 0);
            mp.seekTo(xiabiao);
            mp.start();
            sb.setMax(mp.getDuration());
            timer = new Timer();
            task = new TimerTask() {

                @Override
                public void run() {

                    int currentPosition = mp.getCurrentPosition();
                    sb.setProgress(currentPosition);

                }
            };
            timer.schedule(task, 0, 100);

        }

    }

    class MyOnCompletionListener implements OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            video_play_again.setVisibility(View.VISIBLE);
            iv_play.setVisibility(View.VISIBLE);
            iv_stop.setVisibility(View.GONE);
            sb.setVisibility(View.VISIBLE);
            isplaying=false;
        }

    }






}
