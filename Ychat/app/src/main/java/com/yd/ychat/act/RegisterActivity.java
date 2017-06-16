package com.yd.ychat.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.yd.ychat.R;

/**
 * Created by 荀高杰 on 2017/4/21.
 */

public class RegisterActivity extends BaseActivity {
    private  EditText register_user;
    private  EditText register_password;
    private  EditText register_password2;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    finish();
                    showToast("注册成功");
                    break;
                case 0:
                showToast("注册失败");
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initview();

    }

    private void initview() {
       register_user= (EditText) findViewById(R.id.register_user);
       register_password= (EditText) findViewById(R.id.register_password);
        register_password2=(EditText) findViewById(R.id.register_password2);
}
    public void register(View v){
        final String user = register_user.getText().toString();
        final String password = register_password.getText().toString();
        String password2 = register_password2.getText().toString();
        if(TextUtils.isEmpty(user)||user.length()>12||user.length()<6){
            showToast("账号格式有误");
            return;
        }

        if(TextUtils.isEmpty(password)||password.length()>12||password.length()<6){
            showToast("密码格式有误");
            return;
        }
        if(!password.equals(password2)){
            showToast("两次密码输入不同");
            return;
        }




            new Thread(){
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().createAccount(user, password);
                        handler.sendEmptyMessage(1);
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                       handler.sendEmptyMessage(0);


                    }

                }
            }.start();

    }
}
