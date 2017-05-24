package com.yd.ychat.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 荀高杰 on 2017/4/27.
 */

public class SPutil {
    public static final String CHAT_DEFF = "chatdeff";
    private static final String  SP_NAME="user";
    private static final String  SP_USERNAME_KEY="username";


    private static SharedPreferences sp;

    public static void setloginsuer(Context context,String username){
        getSp(context);
        sp.edit().putString(SP_USERNAME_KEY,username).apply();
    }
    public static String  getloginsuer(Context context){
        getSp(context);
        return sp.getString(SP_USERNAME_KEY,"");
    }

    public static void setChatDeff(Context context, String json) {
        getSp(context);
        sp.edit().putString(CHAT_DEFF, json).apply();
    }

    public static String getChatDeff(Context context) {
        getSp(context);
        return sp.getString(CHAT_DEFF, "");
    }

    private static void getSp(Context context) {
        if(sp==null){
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
    }


}
