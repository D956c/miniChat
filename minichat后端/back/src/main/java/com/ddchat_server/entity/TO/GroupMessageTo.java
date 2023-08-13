package com.ddchat_server.entity.TO;

import com.ddchat_server.entity.BaseEntity;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)

public class GroupMessageTo extends BaseEntity{
    private String id;
    private String chat_type;
    private String sessionId;
    private String senderId;   ///发送者id
    private String senderName;
    private String senderAvatar;
    private String receiverId; //接收者id
    private String receiverName;
    //private String senderName;
    private String sendTime;  //发送时间
    private String content;  //发送内容
    private String messageType;  //消息类型
    private Integer isWithdrawn;  //是否撤回
}
