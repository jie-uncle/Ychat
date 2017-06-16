package com.yd.ychat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.yd.ychat.R;
import com.yd.ychat.act.ChatActivity;
import com.yd.ychat.act.LoginActivity;
import com.yd.ychat.manager.MessageManager;

/**
 * Created by 荀高杰 on 2017/5/10.
 */

public class SetFragment extends BaseFragment {
    private TextView main_edit_username;
    private TextView main_edit_cotent;
    private Button new_msg_send;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initview(view);
        send();
        view.findViewById(R.id.new_msg_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EMClient.getInstance().logout(true);
                Intent i=new Intent(getContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

    }

    private void send() {
        new_msg_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cotent = main_edit_cotent.getText().toString();
                String username = main_edit_username.getText().toString();
                if(TextUtils.isEmpty(cotent)||TextUtils.isEmpty(username)){
                    Toast.makeText(getContext(),"can not be empty",Toast.LENGTH_SHORT).show();
                }else{
                    EMMessage txtSendMessage = EMMessage.createTxtSendMessage(cotent, username);
                    txtSendMessage.setChatType(EMMessage.ChatType.Chat);
                    EMClient.getInstance().chatManager().sendMessage(txtSendMessage);
                    MessageManager.getInstance().getMessageList().refreshChatList();
                }
            }
        });
    }

    private void initview(View view) {
         main_edit_username= (TextView) view.findViewById(R.id.main_edit_username);
         main_edit_cotent= (TextView) view.findViewById(R.id.main_edit_cotent);
         new_msg_send= (Button) view.findViewById(R.id.new_msg_send);
    }


}

