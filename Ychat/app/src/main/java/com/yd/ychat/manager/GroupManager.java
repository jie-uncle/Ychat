package com.yd.ychat.manager;

import com.yd.ychat.port.GroupList;

/**
 * Created by jie on 2017/6/16.
 */

public class GroupManager {
    private GroupManager(){}
    private static GroupManager gm=new GroupManager();
    private GroupList gl;

    public static GroupManager getInstance (){
        return gm;
    }

    public GroupList getGl() {
        return gl;
    }

    public void setGl(GroupList gl) {
        this.gl = gl;
    }
}
