package com.ddchat_server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("db_comment")
public class Comment extends BaseEntity{
    private String id;
    private String articleId;
    private String userId;
    private String content;
    private String publishTime;
}
