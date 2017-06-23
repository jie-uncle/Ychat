package com.yd.ychat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.yd.ychat.R;
import com.yd.ychat.act.ChatActivity;
import com.yd.ychat.act.GroupActivity;

import java.util.List;
import java.util.logging.Handler;

/**
 * Created by 荀高杰 on 2017/4/27.
 */

public class ContactsFragment extends BaseFragment{
    private ListView fragment_contacts_listview;
    private List<String> usernames;
    private ImageView newfriend,group;

    android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            fragment_contacts_listview.setAdapter(new ArrayAdapter<>(getContext(), R.layout.item_fragment_contacts_listview,usernames));
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_contacts,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        initview(view);
        getData();
        setView();


    }

    private void setView() {

        fragment_contacts_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getContext(), ChatActivity.class);
                i.putExtra("name",usernames.get(position) );
                startActivity(i);
            }
        });
        newfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent2group();
            }
        });
    }

    private void intent2group() {
        Intent i=new Intent(getContext(), GroupActivity.class);
        startActivity(i);
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Message msg = handler.obtainMessage();
                    handler.sendEmptyMessage(1);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initview(View v) {
        fragment_contacts_listview= (ListView) v.findViewById(R.id.fragment_contacts_listview);
        newfriend= (ImageView) v.findViewById(R.id.cantacts_iv_newfriend);
        group= (ImageView) v.findViewById(R.id.cantacts_iv_group);
    }
}
