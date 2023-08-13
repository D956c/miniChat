package com.ddchat_server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("db_group_message")
public class GroupMessage extends BaseEntity{
    private String id;
    private String groupId;
    private String senderId;   ///发送者id
    private String senderName; //发送者昵称
    private String avatar;   //发送者头像
    private String sendTime;  //发送时间
    private String content;  //发送内容
    private String messageType;  //消息类型
    private Integer isWithdrawn;  //是否撤回
}

