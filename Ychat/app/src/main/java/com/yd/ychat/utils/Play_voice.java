package com.yd.ychat.utils;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by jie on 2017/6/5.
 */

public class Play_voice {
    private static MediaPlayer my = new MediaPlayer();

    public static void play(String url) {
        my.reset();
        try {
            my.setDataSource(url);
            my.prepareAsync();
            my.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    my.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
