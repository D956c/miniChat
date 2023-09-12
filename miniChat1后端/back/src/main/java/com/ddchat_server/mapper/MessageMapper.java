package com.ddchat_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ddchat_server.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MessageMapper extends BaseMapper<Message> {

    //撤回消息
    @Update("update db_message " +
            "set is_withdrawn=1 " +
            "where id=#{id}")
    void withdrawMessage(String id);
}
