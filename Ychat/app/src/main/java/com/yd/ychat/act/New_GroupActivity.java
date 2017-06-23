package com.yd.ychat.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;
import com.yd.ychat.R;
import com.yd.ychat.manager.GroupManager;

public class New_GroupActivity extends AppCompatActivity {
    private EditText group_name,group_introduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        getSupportActionBar().setTitle("创建群组");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initview();
    }

    private void initview() {
        group_name= (EditText) findViewById(R.id.group_edit_groupname);
        group_introduction= (EditText) findViewById(R.id.group_edit_groupintroduction);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_creategroup,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_group_create:
                String groupName = group_name.getText().toString();
                String desc = group_introduction.getText().toString();
                if(TextUtils.isEmpty(groupName)||TextUtils.isEmpty(desc)){
                    Toast.makeText(this,"信息不能为空",Toast.LENGTH_SHORT).show();

                }else{
                    EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
                    option.maxUsers = 200;
                    option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;

                    String reason = "邀请加入群";
                    reason  = EMClient.getInstance().getCurrentUser() + reason + groupName;

                    try {
                        EMGroup group = EMClient.getInstance().groupManager().createGroup(groupName, desc, new String[]{}, reason, option);
                        Toast.makeText(this,"创建成功",Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK);
                        finish();
                        GroupManager.getInstance().getGl().refreshGroupList();

                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        Toast.makeText(this,"创建失败"+e,Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }












        return super.onOptionsItemSelected(item);
    }
}
