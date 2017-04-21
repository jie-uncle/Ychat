package com.yd.ychat.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;

import com.hyphenate.chat.EMClient;
import com.yd.ychat.R;

/**
 * Created by 荀高杰 on 2017/4/21.
 */

public class WelcomeActivity extends BaseActivity {
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(EMClient.getInstance().isLoggedInBefore()){
                intent2main();
            }else{
                intent2login();
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        handler.sendEmptyMessageDelayed(0,3000);
        findViewById(R.id.ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeMessages(0);
                handler.sendEmptyMessage(1);
            }
        });
    }
}
