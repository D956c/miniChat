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
@TableName("db_article_like")
public class ArticleLike extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private String id;
    private String userId;
    private String articleId;
}
