package com.ddchat_server.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("db_friend")
public class Friend extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long myId;
    private Long friendId;
    private String notation;
    private Long sessionId;
    private String lastMessage;
    private Integer unread;
    private Boolean isHidden;
}
