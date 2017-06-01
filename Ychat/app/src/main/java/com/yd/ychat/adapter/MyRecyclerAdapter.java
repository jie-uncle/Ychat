package com.yd.ychat.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.yd.ychat.R;
import com.yd.ychat.array.CaogaoMap;
import com.yd.ychat.port.RecyclerViewItemClick;

import java.util.List;
import java.util.Map;


/**
 * Created by 荀高杰 on 2017/4/27.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHouder> {
    private Context context;
    private RecyclerViewItemClick click;
    private List<EMConversation> conversations;
    private Map<String, String> map;





    public MyRecyclerAdapter(Context context, List<EMConversation> conversations) {
        this.conversations = conversations;
        this.context = context;


    }

    @Override
    public MyViewHouder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyViewHouder(LayoutInflater.from(context).inflate(R.layout.item_chatlist, parent, false));
    }

    class MyViewHouder extends RecyclerView.ViewHolder {
        private TextView item_chat_news;
        private TextView item_chat_time;
        private TextView item_chat_unreadnumber;
        private TextView item_chat_username;
        private LinearLayout item_chat_lay;
        private  View item_chat_caogao;
        private View item_chatlist_count_lay;

        public MyViewHouder(View itemView) {
            super(itemView);
            initview(itemView);
        }

        private void initview(View view) {
            item_chat_username = (TextView) view.findViewById(R.id.item_chat_username);
            item_chat_news = (TextView) view.findViewById(R.id.item_chat_news);
            item_chat_time = (TextView) view.findViewById(R.id.item_chat_time);
            item_chat_unreadnumber = (TextView) view.findViewById(R.id.item_chat_unreadnumber);
            item_chat_lay = (LinearLayout) view.findViewById(R.id.item_chat_lay);
             item_chat_caogao = view.findViewById(R.id.item_chat_caogao);
             item_chatlist_count_lay = view.findViewById(R.id.item_chatlist_count_lay);
        }
    }


    @Override
    public void onBindViewHolder(MyViewHouder holder, final int position) {
        if(map==null){
            map = CaogaoMap.getInstance();
        }



        //获取消息对象
        EMConversation emConversation = conversations.get(position);

        //获取消息内容
        EMConversation.EMConversationType type = emConversation.getType();
        //获取用户名
        String userName = emConversation.getUserName();
        //获取未读消息数
        int unreadMsgCount = emConversation.getUnreadMsgCount();
        String unreadcount=String.valueOf(unreadMsgCount);
        if(unreadMsgCount>99){
            unreadcount="99+";
        }
        //获取最后一条消息
        EMMessage lastMsg = emConversation.getLastMessage();
        //获取消息类型
        EMMessage.Type msgType = lastMsg.getType();
        //获取消息时间
        long msgTime = lastMsg.getMsgTime();


        switch (msgType){
            case TXT:
                EMTextMessageBody txtmsg = (EMTextMessageBody) lastMsg.getBody();
                //消息内容
                holder.item_chat_news.setText(txtmsg.getMessage());
                break;
            case IMAGE:
                holder.item_chat_news.setText("[图片]");
                break;
            case VIDEO:
                holder.item_chat_news.setText("[视频]");
                break;
            case VOICE:
                holder.item_chat_news.setText("[语音]");
                break;
        }

        if(TextUtils.isEmpty(map.get(userName))){

            holder.item_chat_caogao.setVisibility(View.GONE);

        }else{
            //获取草稿内容
            String caogao =map.get(userName);


            holder.item_chat_caogao.setVisibility(View.VISIBLE);
            holder.item_chat_news.setText(caogao);

        }
//        String draft = sp.getString(userName, "");
//        if(TextUtils.isEmpty(draft)){
//            holder.item_chat_caogao.setVisibility(View.GONE);
//        }else{
//            holder.item_chat_caogao.setVisibility(View.VISIBLE);
//                holder.item_chat_news.setText(draft);
//        }

        holder.item_chat_username.setText(userName);
        holder.item_chat_unreadnumber.setText(unreadcount);
        if(unreadMsgCount==0){
            holder.item_chatlist_count_lay.setVisibility(View.GONE);
        }else {
            holder.item_chatlist_count_lay.setVisibility(View.VISIBLE);
        }
        holder.item_chat_time.setText("");

        holder.item_chat_lay.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                click.onitemlongclick(position);
                return true;
            }
        });
        holder.item_chat_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onitemclicklistener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public void setClick(RecyclerViewItemClick click) {
        this.click = click;
    }
}
