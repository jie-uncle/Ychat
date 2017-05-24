package com.yd.ychat.array;

import com.yd.ychat.manager.MessageManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 荀高杰 on 2017/5/17.
 */

public class CaogaoMap {
    private static Map<String,String> map=new HashMap<>();

    public static Map<String, String> getInstance (){
            return map;
    }
}
