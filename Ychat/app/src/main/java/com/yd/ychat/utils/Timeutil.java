package com.yd.ychat.utils;


import android.icu.util.Calendar;

import java.util.Date;

/**
 * Created by 荀高杰 on 2017/5/5.
 */

public class Timeutil {
    private static final long DAY_TIME=1000*60*60*24;
    public static void  getTime(long millis){
        String time;

        long currentTimeMillis = System.currentTimeMillis();
        
        long time_log = currentTimeMillis - millis;
        Date date=new Date(millis);
        Date date2=new Date(currentTimeMillis);
        if(date.getYear()==date2.getYear()){
            int hours = date.getHours();
            int minutes = date.getMinutes();
            if(minutes<10){
                time=hours+":0"+minutes;
            }else {
                time=hours+":"+minutes;
            }
        }else if(time_log<2*DAY_TIME){
            time="昨天";
        }else if(time_log<7*DAY_TIME){

        }else{
            date.getMonth();
            date.getDay();

        }

    }
}
