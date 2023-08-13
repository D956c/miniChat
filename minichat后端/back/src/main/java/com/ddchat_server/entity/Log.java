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
@TableName("db_log")
public class Log extends BaseEntity{
    @TableId(type = IdType.AUTO)
    private Long id;
    private String type;  //日志类型
    private Long userId;
    private String information;  //日志信息
}
