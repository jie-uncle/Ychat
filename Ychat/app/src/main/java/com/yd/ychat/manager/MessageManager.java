package com.yd.ychat.manager;

import com.yd.ychat.port.MessageList;

/**
 * Created by 荀高杰 on 2017/5/10.
 */

public class MessageManager {
    private MessageList messageList;
    private static MessageManager messageManager=new MessageManager();

    public static MessageManager getInstance (){
        return messageManager;
    }

    public MessageList getMessageList() {
        return messageList;
    }

    public void setMessageList(MessageList messageList) {
        this.messageList = messageList;
    }
}
