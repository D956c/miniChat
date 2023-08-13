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
@TableName("db_member")
public class Member extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long memberId;
    private String groupId;
    private Integer unread;
    private String lastMessage;
    private Boolean isHidden;
}
