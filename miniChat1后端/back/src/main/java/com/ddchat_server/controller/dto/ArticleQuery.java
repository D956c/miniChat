package com.ddchat_server.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleQuery {
    private String userId;
    private String authority;
    private String indexId;
    private String articleId;
    private String type;
    private String commentId;
    private String personId;
    private String condition = "";  //条件
}
