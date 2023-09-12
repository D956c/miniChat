package com.ddchat_server.service;

import com.ddchat_server.entity.GroupMessage;
import com.ddchat_server.entity.Message;

public interface MessageService {
    void storeMessage(Message message);
    void StoreGroupMessage(GroupMessage groupMessage);
    void withdrawMessage(String id);
    void withdrawGroupMessage(String id);

    void updateMsgWithdrawnSession(String sessionId);

    void updateGroupMsgWithdrawnSession(String groupId,String name);
}
