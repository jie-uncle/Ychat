package com.yd.ychat.act;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.yd.ychat.R;
import com.yd.ychat.adapter.ChatRecyclerAdapter;
import com.yd.ychat.array.CaogaoMap;
import com.yd.ychat.fragment.ImageFragment;
import com.yd.ychat.fragment.YuyinFragmrnt;
import com.yd.ychat.fragment.faceFragment;
import com.yd.ychat.manager.MessageManager;
import com.yd.ychat.utils.SPutil;

import java.util.List;
import java.util.Map;

import static com.yd.ychat.R.id.action_bar;
import static com.yd.ychat.R.id.chat_bobottom_flay;
import static com.yd.ychat.R.id.chat_message_image_lay;
import static com.yd.ychat.R.id.message_fragment_face_recyclerview;

public class ChatActivity extends BaseActivity implements View.OnClickListener, EMMessageListener {
    public static final int FRAGMENT_CLOSE = 0;
    private  boolean flay=false;
    private View chat_bobottom_flay;
    private ImageView chat_iv_yuyin;
    private ImageView chat_iv_tupian;

    private ImageView chat_iv_biaoqing;
    private ImageView chat_iv_genduo;
    public EditText chat_edit_msg_content;
    private Button chat_msg_send;
    private String name;
    private RecyclerView chat_recyclerview;
    private  List<EMMessage> messages;
    private ChatRecyclerAdapter adapter;
    private Map<String,String> map;
    private EMConversation conversation;
    private ImageFragment imagefragment;
    private faceFragment  faceFragment;
    private YuyinFragmrnt yuyinFragmrnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initview();
        getdata();
        adapter=new ChatRecyclerAdapter(messages,this,name);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        chat_recyclerview.setLayoutManager(llm);
        Log.e("haha",messages.size()+"");
        chat_recyclerview.setAdapter(adapter);
        chat_recyclerview.scrollToPosition(messages.size()-1);


    }

    private void getdata() {
        conversation= EMClient.getInstance().chatManager().getConversation(name);
        Log.e("data",name);
//获取此会话的所有消息

        messages= conversation.getAllMessages();
        conversation.markAllMessagesAsRead();
        messages = conversation.loadMoreMsgFromDB(messages.get(0).getMsgId(),20);
        messages=conversation.getAllMessages();

//SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
//获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
//       messages = conversation.loadMoreMsgFromDB( conversation.getLastMessage().getMsgId(), 35);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_message,menu);
        return true;
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

    private void initview() {
        //初始化组件
        chat_bobottom_flay = findViewById(R.id.chat_bobottom_flay);
        chat_iv_yuyin= (ImageView) findViewById(R.id.chat_iv_yuyin);
        chat_iv_tupian= (ImageView) findViewById(R.id.chat_iv_tupian);
        chat_iv_biaoqing= (ImageView) findViewById(R.id.chat_iv_biaoqing);
        chat_iv_genduo= (ImageView) findViewById(R.id.chat_iv_genduo);



        final SwipeRefreshLayout chat_Swipereshlay= (SwipeRefreshLayout) findViewById(R.id.chat_Swipereshlay);
        chat_edit_msg_content= (EditText) findViewById(R.id.chat_edit_msg_content);
        chat_msg_send= (Button) findViewById(R.id.chat_msg_send);
        chat_recyclerview= (RecyclerView) findViewById(R.id.chat_recyclerview);

        chat_recyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float startY = 0;
                switch (event.getAction()){

                    case MotionEvent.ACTION_MOVE:
                        HideKey();
                        if(imagefragment.isVisible()){
                            bottom_fly_open_or_close(FRAGMENT_CLOSE);
                        }





                       break;

                }
                return false;
            }
        });

        imagefragment=new ImageFragment();
        faceFragment=new faceFragment();
         yuyinFragmrnt = new YuyinFragmrnt();

        //初始化map
        map=CaogaoMap.getInstance();
        //设置监听（还未实现）
        chat_iv_tupian.setOnClickListener(this);
        chat_iv_yuyin.setOnClickListener(this);

        chat_iv_biaoqing.setOnClickListener(this);
        chat_iv_genduo.setOnClickListener(this);

        chat_msg_send.setOnClickListener(this);
        chat_edit_msg_content.setOnClickListener(this);

        chat_Swipereshlay.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                messages= conversation.loadMoreMsgFromDB(messages.get(0).getMsgId(),10);
                messages=conversation.getAllMessages();
                adapter.notifyDataSetChanged();
                chat_Swipereshlay.setRefreshing(false);


            }
        });
        //消息的监听
        EMClient.getInstance().chatManager().addMessageListener(this);
        //在Map中获取草稿内容
        String draft =map .get(name);

        if(!TextUtils.isEmpty(draft)){
            chat_edit_msg_content.setText(draft);
        }else{
            chat_msg_send.setEnabled(false);
        }


        //给输入框设置监听
        chat_edit_msg_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0){
                    chat_msg_send.setEnabled(false);
                }else {
                    chat_msg_send.setEnabled(true);
                }
            }
        });
        chat_edit_msg_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    chat_bobottom_flay.setFocusable(true);
                    HideKey();
                }
            }
        });

    }

    private void click() {
        if(flay==false){
            chat_bobottom_flay.setVisibility(View.VISIBLE);
        }else{

        }
        flay=!flay;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_msg_send:
                String txt = chat_edit_msg_content.getText().toString();
                creatTxtmsg(txt);
                //发送消息
                chat_edit_msg_content.setText("");

                break;
            case R.id.chat_edit_msg_content:
                bottom_fly_open_or_close(FRAGMENT_CLOSE);
                break;
            case R.id.chat_iv_biaoqing:
                bottom_fly_open_or_close(R.id.chat_iv_biaoqing);

                HideKey();
                break;
            case R.id.chat_iv_tupian:
                bottom_fly_open_or_close(R.id.chat_iv_tupian);
                HideKey();

                break;
            case R.id.chat_iv_yuyin:
                bottom_fly_open_or_close(R.id.chat_iv_yuyin);
                HideKey();
                break;
            case R.id.chat_iv_genduo:
                HideKey();
                break;

        }
        
    }
    //隐藏软键盘
    private void HideKey() {
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(chat_edit_msg_content.getWindowToken(),0);
    }

    private void bottom_fly_open_or_close(int res) {
        FragmentTransaction ft_image = null;
        FragmentTransaction ft_face = null;
        FragmentTransaction ft_yuyin = null;


        if(res==R.id.chat_iv_tupian){
            FragmentManager fm = getSupportFragmentManager();
            if(imagefragment.isVisible()){
                closeFragment(fm,imagefragment);
            }else{
                if(faceFragment.isVisible()){
                    replaceFragment(ft_face,fm,faceFragment,imagefragment);
                }else if(yuyinFragmrnt.isVisible()){
                    replaceFragment(ft_yuyin,fm,yuyinFragmrnt,imagefragment);
                }else{
                    addFragment(fm,imagefragment);
                }
            }
        }else if(res==R.id.chat_iv_biaoqing){
            FragmentManager fm = getSupportFragmentManager();
            if(faceFragment.isVisible()){
                closeFragment(fm,faceFragment);
            }else{
                if(imagefragment.isVisible()){
                    replaceFragment(ft_image, fm,imagefragment,faceFragment);
                }else if(yuyinFragmrnt.isVisible()){
                    replaceFragment(ft_yuyin,fm,yuyinFragmrnt,faceFragment);
                }else{
                    addFragment(fm,faceFragment);
                }
            }

        }else if(res==R.id.chat_iv_yuyin){
            FragmentManager fm = getSupportFragmentManager();
            if(yuyinFragmrnt.isVisible()){
                closeFragment(fm,yuyinFragmrnt);
            }else{
                if(imagefragment.isVisible()){
                    replaceFragment(ft_image, fm,imagefragment,yuyinFragmrnt);
                }else if(faceFragment.isVisible()){
                    replaceFragment(ft_yuyin,fm,faceFragment,yuyinFragmrnt);
                }else{
                    addFragment(fm,yuyinFragmrnt);
                }
            }




        }else if(res==FRAGMENT_CLOSE){
            FragmentManager fm = getSupportFragmentManager();

            if(imagefragment.isAdded()){
                closeFragment(fm,imagefragment);
            }
            if(faceFragment.isAdded()){
                closeFragment(fm,faceFragment);
            }
            if(yuyinFragmrnt.isAdded()){
                closeFragment(fm,yuyinFragmrnt);
            }
        }


    }

    private void addFragment(FragmentManager fm,Fragment addfragment) {
        FragmentTransaction ft_face;
        ft_face = fm.beginTransaction();
        ft_face.add(R.id.chat_bobottom_flay,addfragment);
        ft_face.addToBackStack(null);
        ft_face.commit();
    }

    /**
     *
     * @param ft_image 就是加载 removeFragment 的FragmentTransaction
     * @param fm        fragment的管理器
     * @param removeFragment  要移除的fragment
     * @param addFranment       要显示的fragment
     */
    private void replaceFragment(FragmentTransaction ft_image, FragmentManager fm,Fragment removeFragment,Fragment addFranment) {
        fm.popBackStack();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if(ft_image!=null){
            ft_image.remove(removeFragment);
        }
        fragmentTransaction.add(R.id.chat_bobottom_flay,addFranment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     *
     * @param fm
     * @param fragment 要关闭的fragment
     */
    private void closeFragment(FragmentManager fm, Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();
        fm.popBackStack();
    }

    @Override
    protected void onDestroy() {
        //移出消息监听
        EMClient.getInstance().chatManager().removeMessageListener(this);
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        获取输入框的内容
        String msg = chat_edit_msg_content.getText().toString();
        if(TextUtils.isEmpty(msg)){
//            内容为空 在map中删除
            map.remove(name);
        }else{
//            不为空跟新map中的数据
            map.put(name,msg);
        }
        MessageManager.getInstance().getMessageList().refreshChatList();
//                把集合存入sp
        SPutil.setChatDeff(ChatActivity.this,new Gson().toJson(map));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onMessageReceived(final List<EMMessage> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EMMessage emMessage = list.get(0);
//                判断新的消息是不是当前联系人发来的
                if(emMessage.getFrom()==name){

                    messages.addAll(list);
                    adapter.notifyDataSetChanged();
                }
                MessageManager.getInstance().getMessageList().refreshChatList();
            }
        });


    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    public void creatTxtmsg(String txt){
        EMMessage txtSendMessage = EMMessage.createTxtSendMessage(txt, name);
        sendmsg(txtSendMessage);

    }
    public void creatImagemsg(String path){
        EMMessage imageSendMessage = EMMessage.createImageSendMessage(path, false, name);
        sendmsg(imageSendMessage);
    }

    public void creatYuyin(String path,int time){
        EMMessage voiceSendMessage = EMMessage.createVoiceSendMessage(path, time, name);
        sendmsg(voiceSendMessage);
    }

    public void sendmsg(EMMessage msg){
        msg.setChatType(EMMessage.ChatType.Chat);
        EMClient.getInstance().chatManager().sendMessage(msg);
        messages.add(msg);
        adapter.notifyDataSetChanged();
        chat_recyclerview.scrollToPosition(messages.size()-1);

      msg.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MessageManager.getInstance().getMessageList().refreshChatList();
                }
            });


            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });






    }
}
