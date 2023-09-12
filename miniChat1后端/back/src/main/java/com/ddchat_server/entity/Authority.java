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
@TableName("db_authority")
public class Authority extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Long id;
    private String type;  //权限类型{login-forbidden:禁止登录}
    private Long userId;
    private String information;  //权限类型
}
