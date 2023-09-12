package com.ddchat_server.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddchat_server.entity.Friend;
import com.ddchat_server.mapper.FriendMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class FriendServiceImp implements FriendService{

    @Resource
    private FriendMapper friendMapper;
    @Override
    @Transactional
    public Boolean checkIfFriend(String id1, String id2) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",false).eq("my_id",id1).eq("friend_id", id2);
        return friendMapper.selectCount(queryWrapper) > 0;
    }
}
