package com.ddchat_server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("db_comment_like")
public class CommentLike extends BaseEntity{
    private String id;
    private String commentId;
    private String userId;
}
