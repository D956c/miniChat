package com.ddchat_server.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
    public Date createTime = new Date();  //创建时间
    public Boolean isDeleted = false;    //逻辑删除标志
    public Date updateTime = new Date();  //更新时间
}
