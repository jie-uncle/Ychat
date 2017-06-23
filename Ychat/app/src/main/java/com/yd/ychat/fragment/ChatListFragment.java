package com.yd.ychat.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.yd.ychat.R;
import com.yd.ychat.act.ChatActivity;
import com.yd.ychat.act.GroupChatActivity;
import com.yd.ychat.adapter.MyRecyclerAdapter;
import com.yd.ychat.array.CaogaoMap;
import com.yd.ychat.manager.MessageManager;
import com.yd.ychat.port.MessageList;
import com.yd.ychat.port.RecyclerViewItemClick;
import com.yd.ychat.utils.SPutil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by 荀高杰 on 2017/4/27.
 */

public class ChatListFragment extends BaseFragment implements MessageList {
    public static final int CAOGAO_CODE = 101;
    private RecyclerView recyclerview;
    private Map<String, EMConversation> conversations;
    private List<EMConversation> list = new ArrayList<>();
    private static final int LINE_HEIGHT = 2;
    private static MyRecyclerAdapter adapter;






    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EMClient.getInstance().chatManager().loadAllConversations();
        getnews();
        initview(view);



    }

    private void getnews() {
        conversations = EMClient.getInstance().chatManager().getAllConversations();
        Collection<EMConversation> emConversations = conversations.values();
        Iterator<EMConversation> it = emConversations.iterator();
        while (it.hasNext()) {
            list.add(it.next());
        }
    }

    private void initview(View view) {

        recyclerview = (RecyclerView) view.findViewById(R.id.chat_recyclerview);
        final SwipeRefreshLayout fragment_chatlist_refresh = (SwipeRefreshLayout) view
                .findViewById(R.id.fragment_chatlist_refresh);
        fragment_chatlist_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MessageManager.getInstance().getMessageList().refreshChatList();
                fragment_chatlist_refresh.setRefreshing(false);
            }
        });
        //设置适配器
        adapter = new MyRecyclerAdapter(getActivity(), list);
        //实例化布局管理器
        //线性布局
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        //网格布局    上下文 ，行或列数，朝向，顺序
//        GridLayoutManager glm=new GridLayoutManager(getActivity(),3,LinearLayoutManager.VERTICAL,true);
        //瀑布流布局     行或列数   朝向，，，，，，，，，
//        StaggeredGridLayoutManager sglm=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        //设置布局管理器的朝向
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerview.setLayoutManager(llm);
        recyclerview.setAdapter(adapter);
        setitemclick();
        drawLine();
        MessageManager.getInstance().setMessageList(this);

    }
    private void setitemclick() {
        adapter.setClick(new RecyclerViewItemClick() {
            @Override
            public void onitemclicklistener(int index) {
                if(list.get(index).getType()== EMConversation.EMConversationType.GroupChat){
                    String groupId = EMClient.getInstance().groupManager().getGroup(list.get(index).getLastMessage().getTo()).getGroupId();
                    String groupname = EMClient.getInstance().groupManager().getGroup(list.get(index).getLastMessage().getTo()).getGroupName();
                    intent2Groupchat(groupId,groupname);
                }else if(list.get(index).getType()== EMConversation.EMConversationType.Chat){
                    intent2chat(list.get(index).getUserName());
                }

            }

            @Override
            public void onitemlongclick(final int index) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setTitle("要清除此条会话么？");
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EMClient.getInstance().chatManager().deleteConversation(list.get(index).getUserName(), true);
                        MessageManager.getInstance().getMessageList().refreshChatList();
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    private void intent2chat(String username) {
        Intent i = new Intent(getContext(), ChatActivity.class);
        i.putExtra("name", username);
        startActivityForResult(i,CAOGAO_CODE);
    }

    private void intent2Groupchat(String groupid,String groupname) {
        Intent i = new Intent(getContext(), GroupChatActivity.class);
        i.putExtra("groupid", groupid);
        i.putExtra("groupname",groupname);
        startActivityForResult(i, CAOGAO_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case CAOGAO_CODE:

                    break;
            }
        }



        super.onActivityResult(requestCode, resultCode, data);

    }

    private void drawLine() {
        recyclerview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDrawOver(c, parent, state);
                int left = parent.getPaddingLeft();
                int reght = parent.getMeasuredWidth() - parent.getPaddingRight();
                int childCount = parent.getChildCount();
                int top = 0;
                int bottom = 0;
                for (int i = 0; i < childCount; i++) {
                    View v = parent.getChildAt(i);
                    top = v.getBottom();
                    bottom = v.getBottom() + LINE_HEIGHT;

                    c.drawRect(left, top, reght, bottom, new Paint());

                }
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.contains(0, 0, 0, LINE_HEIGHT);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void refreshchat() {
        list.clear();
        getnews();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void refreshChatList() {

        refreshchat();
    }
}
