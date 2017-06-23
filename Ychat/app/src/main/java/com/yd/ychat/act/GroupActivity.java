package com.yd.ychat.act;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.yd.ychat.R;
import com.yd.ychat.adapter.GroupAdapter;
import com.yd.ychat.manager.GroupManager;
import com.yd.ychat.port.GroupList;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private List<EMGroup> grouplist=new ArrayList<>();
    private GroupAdapter ga;
    private View creat_group;
    private ListView group_list;
    private SwipeRefreshLayout srl;



    Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            srl.setRefreshing(false);
            switch (msg.what) {
                case 0:
                    refresh();
                    break;
                case 1:
                    Toast.makeText(GroupActivity.this, "121", Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        getSupportActionBar().setTitle("群组");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initview();
        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        ga=new GroupAdapter(grouplist,this);
        group_list.setAdapter(ga);
        creat_group.setOnClickListener(this);
        srl.setOnRefreshListener(this);
        GroupManager.getInstance().setGl(new GroupList() {
            @Override
            public void refreshGroupList() {
                refresh();
            }
        });
        group_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(GroupActivity.this,GroupChatActivity.class);
                i.putExtra("groupid",grouplist.get(position).getGroupId());
                i.putExtra("groupname",grouplist.get(position).getGroupName());
                Log.e("hehe",grouplist.get(position).getGroupId()+grouplist.get(position).getGroupName());
                startActivity(i);
            }
        });

    }

    private void initview() {
         creat_group = findViewById(R.id.group_newgroup);
         group_list= (ListView) findViewById(R.id.group_grouplist);
         srl= (SwipeRefreshLayout) findViewById(R.id.swiperefresh_grouplist);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.group_newgroup:
                Intent i=new Intent(GroupActivity.this,New_GroupActivity.class);
//                startActivityForResult(i,1221);
                startActivity(i);
                break;
        }
    }



    @Override
    public void onRefresh() {
        getGroupListfromService();
    }

    private void getGroupListfromService() {
        new Thread(){
            @Override
            public void run(){
                try {
                    EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    handler.sendEmptyMessage(0);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    private void refresh(){
        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        ga=new GroupAdapter(grouplist,this);
        group_list.setAdapter(ga);
        ga.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
