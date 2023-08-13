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
@TableName("db_article")
public class Article extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private String id;
    private String userId;
    private Integer authority; //访问权限 0：全部人可见，1：仅好友可见
    private String publishTime;
    private String content;
    private String imgList;
    private Integer viewNum; //查阅数量
}
