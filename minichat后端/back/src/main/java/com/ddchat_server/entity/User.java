package com.ddchat_server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("db_user")
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Long id;
    private String email;
    private String nickname;
    private String avatar;
    private Integer gender;
    private String password;
    private String background; //背景图
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date birthday;
    private String introduction; //介绍
    private Boolean isActivated; //是否被激活

    public void clearPrivateInfo(){
        this.password = null;
        this.isActivated = null;
        this.updateTime = null;
        this.isDeleted = null;
    }
}
