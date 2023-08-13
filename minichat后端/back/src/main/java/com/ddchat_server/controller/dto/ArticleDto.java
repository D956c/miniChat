package com.ddchat_server.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    String id;
    String userId;
    String avatar;
    String name;
    String publishTime;
    String content;
    String imgList;
    Boolean isLike;
    Integer likeNum;
    Integer commentNum;
    Integer viewNum;
    Boolean isFriend;
    Integer authority;
}
