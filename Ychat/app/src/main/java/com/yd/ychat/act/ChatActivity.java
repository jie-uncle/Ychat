package com.yd.ychat.act;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.yd.ychat.R;
import com.yd.ychat.adapter.ChatRecyclerAdapter;
import com.yd.ychat.array.CaogaoMap;
import com.yd.ychat.fragment.BaseFragment;
import com.yd.ychat.fragment.ImageFragment;
import com.yd.ychat.fragment.YuyinFragmrnt;
import com.yd.ychat.fragment.faceFragment;
import com.yd.ychat.manager.MessageManager;
import com.yd.ychat.utils.SPutil;
import com.yd.ychat.utils.StringUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.yd.ychat.R.id.action_bar;
import static com.yd.ychat.R.id.chat_bobottom_flay;
import static com.yd.ychat.R.id.chat_message_image_lay;
import static com.yd.ychat.R.id.message_fragment_face_recyclerview;
import static com.yd.ychat.R.id.time;

public class ChatActivity extends BaseActivity implements View.OnClickListener, EMMessageListener {
    public static final int FRAGMENT_CLOSE = 0;
    private boolean flay = false;
    private View chat_bobottom_flay;
    private ImageView chat_iv_yuyin;
    private ImageView chat_iv_tupian;
    private ImageView chat_iv_video;

    private ImageView chat_iv_biaoqing;
    private ImageView chat_iv_genduo;
    public EditText chat_edit_msg_content;
    private Button chat_msg_send;
    private String name;
    private static RecyclerView chat_recyclerview;
    private List<EMMessage> messages = new ArrayList<>();
    private ChatRecyclerAdapter adapter;
    private Map<String, String> map;
    private EMConversation conversation;
    private ImageFragment imagefragment;
    private faceFragment faceFragment;
    private YuyinFragmrnt yuyinFragmrnt;
    private BaseFragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);
        name = getIntent().getStringExtra("name");
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initview();
        if (getdata() != null) {
            messages.addAll(getdata());
        }
        adapter = new ChatRecyclerAdapter(messages, this, name);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        chat_recyclerview.setLayoutManager(llm);
        chat_recyclerview.setAdapter(adapter);
        chat_recyclerview.scrollToPosition(messages.size() - 1);


    }

    private List<EMMessage> getdata() {
        conversation = EMClient.getInstance().chatManager().getConversation(name);
        if (conversation == null) {
            return null;
        }
//获取此会话的所有消息
        conversation.markAllMessagesAsRead();
        List<EMMessage> messageList = conversation.getAllMessages();

        if (messageList.size() == 1) {
            conversation.loadMoreMsgFromDB(messageList.get(0).getMsgId(), 20);
        }


        return conversation.getAllMessages();

//SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
//获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
//       messages = conversation.loadMoreMsgFromDB( conversation.getLastMessage().getMsgId(), 35);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void initview() {
        //初始化组件
        chat_bobottom_flay = findViewById(R.id.chat_bobottom_flay);
        chat_iv_yuyin = (ImageView) findViewById(R.id.chat_iv_yuyin);
        chat_iv_tupian = (ImageView) findViewById(R.id.chat_iv_tupian);
        chat_iv_biaoqing = (ImageView) findViewById(R.id.chat_iv_biaoqing);
        chat_iv_genduo = (ImageView) findViewById(R.id.chat_iv_genduo);
        chat_iv_video = (ImageView) findViewById(R.id.chat_iv_video);


        final SwipeRefreshLayout chat_Swipereshlay = (SwipeRefreshLayout) findViewById(R.id.chat_Swipereshlay);
        chat_edit_msg_content = (EditText) findViewById(R.id.chat_edit_msg_content);
        chat_msg_send = (Button) findViewById(R.id.chat_msg_send);
        chat_recyclerview = (RecyclerView) findViewById(R.id.chat_recyclerview);

        chat_recyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float startY = 0;
                switch (event.getAction()) {

                    case MotionEvent.ACTION_MOVE:
                        HideKey();
                        closefragment();
                        break;
                }
                return false;
            }
        });

        imagefragment = new ImageFragment();
        faceFragment = new faceFragment();
        yuyinFragmrnt = new YuyinFragmrnt();

        //初始化map
        map = CaogaoMap.getInstance();
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
                messages = conversation.loadMoreMsgFromDB(messages.get(0).getMsgId(), 10);
                messages = conversation.getAllMessages();
                adapter.notifyDataSetChanged();
                chat_Swipereshlay.setRefreshing(false);


            }
        });
        //消息的监听
        EMClient.getInstance().chatManager().addMessageListener(this);
        //在Map中获取草稿内容
        String draft = map.get(name);

        if (!TextUtils.isEmpty(draft)) {
            SpannableStringBuilder newmsg = StringUtil.handler(this,draft);
            chat_edit_msg_content.setText(newmsg);
        } else {
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
                if (s.length() == 0) {
                    chat_msg_send.setEnabled(false);
                } else {
                    chat_msg_send.setEnabled(true);
                }
            }
        });
        chat_edit_msg_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    chat_bobottom_flay.setFocusable(true);
                    HideKey();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chat_msg_send:
                String txt = chat_edit_msg_content.getText().toString();
                creatTxtmsg(txt);
                //发送消息
                chat_edit_msg_content.setText("");
                break;
            case R.id.chat_edit_msg_content:
                closefragment();
                break;
            case R.id.chat_iv_biaoqing:
                bottom_fly_open_or_close(faceFragment);

                HideKey();
                break;
            case R.id.chat_iv_tupian:
                bottom_fly_open_or_close(imagefragment);
                HideKey();

                break;
            case R.id.chat_iv_yuyin:
                bottom_fly_open_or_close(yuyinFragmrnt);
                HideKey();
                break;
            case R.id.chat_iv_genduo:
                HideKey();
                break;
            case R.id.chat_iv_video:
                Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(i, 1011);
                HideKey();
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
                this.creatVideo(data);
        }
    }

    //隐藏软键盘
    private void HideKey() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(chat_edit_msg_content.getWindowToken(), 0);
    }

    private void bottom_fly_open_or_close(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        if (fragment.isVisible()) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(fragment);
            ft.commit();
            currentFragment = null;
        } else {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.chat_bobottom_flay, fragment);
            ft.commit();
            currentFragment = (BaseFragment) fragment;
        }
    }

    private boolean closefragment() {
        if (currentFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(currentFragment)
                    .commit();
            currentFragment = null;
            return true;
        }
        return false;
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
        if (TextUtils.isEmpty(msg)) {
//            内容为空 在map中删除
            map.remove(name);
        } else {
//            不为空跟新map中的数据
            map.put(name, msg);
        }
        MessageManager.getInstance().getMessageList().refreshChatList();
//                把集合存入sp
        SPutil.setChatDeff(ChatActivity.this, new Gson().toJson(map));

    }

    @Override
    public void onBackPressed() {
        if (closefragment()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onMessageReceived(final List<EMMessage> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EMMessage emMessage = list.get(0);
//                判断新的消息是不是当前联系人发来的
                if (emMessage.getFrom() .equals(name) ) {

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

    public void creatTxtmsg(String txt) {
        EMMessage txtSendMessage = EMMessage.createTxtSendMessage(txt, name);
        sendmsg(txtSendMessage);

    }

    public void creatImagemsg(String path) {
        EMMessage imageSendMessage = EMMessage.createImageSendMessage(path, false, name);
        sendmsg(imageSendMessage);
    }

    public void creatYuyin(String path, int time) {
        EMMessage voiceSendMessage = EMMessage.createVoiceSendMessage(path, time, name);
        sendmsg(voiceSendMessage);
    }

    public void creatVideo(Intent data) {
        String viodepath = getviodepath(data);
            File imagefile = getImagefile(viodepath);
            int videotime = getvideotime(viodepath);
            EMMessage videoSendMessage = EMMessage.createVideoSendMessage(viodepath, imagefile.getAbsolutePath(), videotime, name);
            sendmsg(videoSendMessage);

    }

    private File getImagefile(String viodepath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(viodepath);
        Bitmap bitmap = mmr.getFrameAtTime(1000);
        String filename = System.currentTimeMillis() + ".jpg";
        File file = new File(this.getCacheDir()+"/"+filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            OutputStream os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG
                    , 50
                    , os
            );
            os.flush();
            os.close();
            os = null;
            bitmap.recycle();
            bitmap = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private int getvideotime(String viodepath) {
        int duration = 0;
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(viodepath);
            duration = mp.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.reset();
        mp.release();
        mp=null;
        return duration;
    }

    private String getviodepath(Intent data) {
        String videopath = null;
        Cursor cursor = managedQuery(data.getData()
                , new String[]{MediaStore.Video.Media.DATA}
                , null
                , null
                , null
        );
        if (cursor != null) {
            if(cursor.moveToNext()){
                videopath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            }

        }
        cursor.close();
        return videopath;
    }

    public void sendmsg(EMMessage msg) {
        msg.setChatType(EMMessage.ChatType.Chat);
        EMClient.getInstance().chatManager().sendMessage(msg);
        messages.add(msg);
        adapter.notifyDataSetChanged();
        chat_recyclerview.scrollToPosition(messages.size() - 1);

        msg.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.e("haha","发送成功");
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

    public static RecyclerView getRecycleview() {
        return chat_recyclerview;
    }
}
