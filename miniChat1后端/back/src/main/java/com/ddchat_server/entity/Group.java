package com.ddchat_server.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("db_group")
public class Group extends BaseEntity{
    private Long id;
    private String name;
    private String avatar;
    private String introduction;
    private Long ownerId;
}
