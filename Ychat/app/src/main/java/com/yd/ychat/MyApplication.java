package com.yd.ychat;

import android.app.Application;



import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

/**
 * Created by 荀高杰 on 2017/4/21.
 * 生命周期与程序一制
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initEM();
    }

    private void initEM() {
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);

//初始化
        EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }
}
