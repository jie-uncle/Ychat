package com.yd.ychat.act;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.yd.ychat.R;
import com.yd.ychat.act.BaseActivity;
import com.yd.ychat.act.WelcomeActivity;
import com.yd.ychat.array.CaogaoMap;
import com.yd.ychat.fragment.BaseFragment;
import com.yd.ychat.fragment.ChatListFragment;
import com.yd.ychat.fragment.ContactsFragment;
import com.yd.ychat.fragment.SetFragment;
import com.yd.ychat.manager.MessageManager;
import com.yd.ychat.utils.SPutil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private Button btn;
    private List<BaseFragment> list;
    private ChatListFragment chatListFragment;
    private ContactsFragment contactsFragment;
    private SetFragment setFragment;
    private static final int CHAT_NUMBER = 0;
    private static final int CONTACT_NUMBER = 1;
    private static final int SET_NUMBER = 2;
    public static final int HANDER_CODE = 505;
    private TabLayout main_tablay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //在sp中获取草稿的json数据
        String chatDeff = SPutil.getChatDeff(this);
        if (!TextUtils.isEmpty(chatDeff)) {
            //把Sp中的数据全部添加到Map集合中
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> map = new Gson().fromJson(chatDeff,type );
            CaogaoMap.getInstance().putAll(map);
        }

        initView();
        initData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        chatListFragment = new ChatListFragment();
        contactsFragment = new ContactsFragment();
        setFragment = new SetFragment();
        //把三个fragment添加到list集合中
        list.add(chatListFragment);
        list.add(contactsFragment);
        list.add(setFragment);
        setmsgadapter();

        main_tablay.setupWithViewPager(viewPager);
        //给tablayout设置点击监听
        tablayclick();
//        设置预存页面数
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(this);
        //设置Tablayout设置初始值
        setitem(main_tablay.getTabAt(0));
    }

    private void tablayclick() {
        main_tablay.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setitem(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * 给viewpager设置adapter
     */
    private void setmsgadapter() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
    }

    private void setitem(TabLayout.Tab tab) {
        main_tablay.getTabAt(0).setIcon(R.drawable.xx_off);
        main_tablay.getTabAt(1).setIcon(R.drawable.lxr_off);
        main_tablay.getTabAt(2).setIcon(R.drawable.qt_off);
        if (tab == main_tablay.getTabAt(0)) {
            tab.setIcon(R.drawable.xx_on);
        } else if (tab == main_tablay.getTabAt(1)) {
            tab.setIcon(R.drawable.lxr_on);
        } else if (tab == main_tablay.getTabAt(2)) {
            tab.setIcon(R.drawable.qt_on);
        }

    }

    private void initView() {
        main_tablay = (TabLayout) findViewById(R.id.main_tablay);
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        //设置消息监听
        setchatlistener();
        list = new ArrayList<>();

    }

    private void setchatlistener() {
        EMMessageListener msgListener = new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatListFragment.refreshchat();
                    }
                });


            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


}
