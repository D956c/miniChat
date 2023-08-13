package com.ddchat_server.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String id;
    private String userId;
    private String avatar;
    private String nickname;
    private String content;
    private String publishTime;
    private Boolean isLike;
    private Integer likeNum;
}
