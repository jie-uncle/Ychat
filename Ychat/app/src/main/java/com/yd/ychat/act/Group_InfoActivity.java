package com.yd.ychat.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.yd.ychat.R;

public class Group_InfoActivity extends AppCompatActivity {
    private TextView groupinfo_groupid,groupinfo_groupname,groupinfo_groupmember_number,groupinfo_groupAdminister;
    private EditText groupinfo_newperson;
    private Button groupinfo_yaoqing;
    private EMGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String groupid = getIntent().getStringExtra("groupid");

        try {
            group = EMClient.getInstance().groupManager().getGroupFromServer(groupid);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        if(group.getOwner().equals(EMClient.getInstance().getCurrentUser())){

        }else{
            setContentView(R.layout.activity_group_info_other);
        }




        groupinfo_groupid= (TextView) findViewById(R.id.groupinfo_groupid);
         groupinfo_groupname= (TextView) findViewById(R.id.groupinfo_groupname);
         groupinfo_groupmember_number= (TextView) findViewById(R.id.groupinfo_groupmember_number);
         groupinfo_groupAdminister= (TextView) findViewById(R.id.groupinfo_groupAdminister_number);
         groupinfo_newperson= (EditText) findViewById(R.id.groupinfo_newperson);
         groupinfo_yaoqing= (Button) findViewById(R.id.groupinfo_yaoqing);
        groupinfo_groupid.setText(groupid);
        groupinfo_groupname.setText(EMClient.getInstance().groupManager().getGroup(groupid).getGroupName());
        groupinfo_groupmember_number.setText(EMClient.getInstance().groupManager().getGroup(groupid).getMemberCount()+"");
        groupinfo_groupAdminister.setText(EMClient.getInstance().groupManager().getGroup(groupid).getOwner());
        groupinfo_groupmember_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Group_InfoActivity.this,Group_MemberlistActivity.class);
                i.putExtra("groupid",groupid);
                startActivity(i);
            }
        });
//        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
////根据群组ID从服务器获取群组基本信息
//        EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(groupId);
//
//        group.getOwner();//获取群主
//        List<String> members = group.getMembers();//获取内存中的群成员
//        List<String> adminList = group.getAdminList();//获取管理员列表
        groupinfo_yaoqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = groupinfo_newperson.getText().toString();
                if(group.getOwner().equals(EMClient.getInstance().getCurrentUser())){
//                    群主加人调用此方法
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().addUsersToGroup(groupid,new String[]{name});//需异步处理
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //私有群里，如果开放了群成员邀请，群成员邀请调用下面方法
                            try {
                                EMClient.getInstance().groupManager().inviteUser(groupid, new String[]{name}, null);//需异步处理
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }


            }
        });


    }
}
