package com.yd.ychat.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hyphenate.chat.EMClient;
import com.yd.ychat.R;

/**
 * 启动页
 */

public class WelcomeActivity extends BaseActivity {
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            intent2next();
        }
    };


    /**
     *跳转下个界面
     */
    private void intent2next() {
        //判断之前是否登录过
        if(EMClient.getInstance().isLoggedInBefore()){
            intent2main();
        }else{
            intent2login();
        }
        WelcomeActivity.this.finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        handler.sendEmptyMessageDelayed(0,3000);
        findViewById(R.id.ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeMessages(0);
                intent2next();
            }
        });

    }
}
