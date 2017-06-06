package com.yd.ychat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.yd.ychat.R;

import java.util.List;

/**
 * Created by 荀高杰 on 2017/4/27.
 */

public class ContactsFragment extends BaseFragment{

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_contacts,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);



            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            }).start();



    }
}
