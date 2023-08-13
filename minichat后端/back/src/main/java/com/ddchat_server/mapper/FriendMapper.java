package com.ddchat_server.mapper;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ddchat_server.controller.FriendController;
import com.ddchat_server.entity.Friend;
import com.ddchat_server.service.MessageServiceImp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FriendMapper extends BaseMapper<Friend> {

    @Select("SELECT f.id,f.friend_id,f.notation,f.session_id,f.last_message,f.unread,f.is_hidden,f.update_time,u.avatar " +
            "FROM db_friend f,db_user u " +
            "where u.id=f.friend_id and f.is_deleted=0 and f.my_id=#{myId}")
    List<JSONObject> getFriendList(String myId);

    ///更新状态
    @Update("update db_friend " +
            "set last_message=#{lastMessage},unread=unread+1,update_time=NOW() " +
            "where session_id=#{sessionId} and is_deleted=0")
    void updateFriend(MessageServiceImp.TestParam param);

    ///清空已读
    @Update("update db_friend " +
            "set unread=0 " +
            "where my_id=#{myId} and friend_id=#{friendId}")
    void clearUnread(FriendController.ClearParam param);

    @Update("update db_friend " +
            "set last_message=#{lastMessage} " +
            "where session_id=#{sessionId} and is_deleted=0")
    void updateSession(MessageServiceImp.TestParam param);

    @Select("SELECT friend_id from db_friend " +
            "WHERE my_id=#{myId} AND is_deleted=0")
    List<String> getFriendIds(String myId);
}
