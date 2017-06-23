package com.yd.ychat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMGroup;
import com.yd.ychat.R;

import java.util.List;

/**
 * Created by jie on 2017/6/16.
 */

public class GroupAdapter extends BaseAdapter {
    private List<EMGroup> list;
    private Context context;

    public GroupAdapter(List<EMGroup> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EMGroup emGroup = list.get(position);

        myviewHouder my;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.group_listview_item,parent,false);
            my=new myviewHouder();

            my.tv_group_name= (TextView) convertView.findViewById(R.id.item_groupname);
            convertView.setTag(my);

        }else{
            my= (myviewHouder) convertView.getTag();
        }
        my.tv_group_name.setText(emGroup.getGroupName ());


        return convertView;
    }
    class myviewHouder{
        TextView tv_group_name;
    }
}
