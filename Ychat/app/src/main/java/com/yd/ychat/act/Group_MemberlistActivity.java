package com.yd.ychat.act;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.util.EMLog;
import com.yd.ychat.R;
import com.yd.ychat.adapter.Group_meberAdapter;
import com.yd.ychat.port.RecyclerViewItemClick;

import java.util.List;

public class Group_MemberlistActivity extends BaseActivity implements RecyclerViewItemClick {
    private String groupid;
    private EMGroup group;
    private List<String> members;
    private RecyclerView group_member_recycleview;
    private Group_meberAdapter ga;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_memberlist);
         groupid = getIntent().getStringExtra("groupid");
         group = EMClient.getInstance().groupManager().getGroupFromServer(groupid,true);
        members = group.getMembers();
         group_member_recycleview= (RecyclerView) findViewById(R.id.group_member_recycleview);
         ga=new Group_meberAdapter(members,this);
        ga.setClick(this);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        group_member_recycleview.setLayoutManager(llm);
        group_member_recycleview.setAdapter(ga);
    }

    @Override
    public void onitemclicklistener(int index) {

    }

    @Override
    public void onitemlongclick(int index) {
        if(group.getOwner().equals(EMClient.getInstance().getCurrentUser())){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            if(){}
            String [] str={"修改","删除"};
            builder.setItems(str, new View.OnClickListener() {



        }
    }
}
    private void isMute(){
        EMClient.getInstance().groupManager().f
    }
}