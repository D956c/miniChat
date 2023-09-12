package com.ddchat_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ddchat_server.controller.FriendController;
import com.ddchat_server.controller.GroupController;
import com.ddchat_server.entity.Member;
import com.ddchat_server.service.MessageServiceImp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MemberMapper extends BaseMapper<Member> {

    @Update("update db_member " +
            "set unread=0 " +
            "where member_id=#{memberId} and group_id=#{groupId}")
    void clearUnread(GroupController.MemberParam memberParam);

    ///更新状态
    @Update("update db_member " +
            "set last_message=#{lastMessage},unread=unread+1,update_time=NOW() " +
            "where group_id=#{sessionId} and is_deleted=0")
    void updateFriend(MessageServiceImp.TestParam param);

    @Update("update db_member " +
            "set last_message=#{lastMessage} " +
            "where group_id=#{sessionId} and is_deleted=0")
    void updateSession(MessageServiceImp.TestParam param);
}
