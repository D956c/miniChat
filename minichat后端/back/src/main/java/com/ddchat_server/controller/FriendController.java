package com.ddchat_server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddchat_server.controller.dto.ResponseDto;
import com.ddchat_server.entity.Friend;
import com.ddchat_server.mapper.FriendMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/ddchat/friend")
public class FriendController {

    @Resource
    private FriendMapper friendMapper;
    //获取好友列表
    @PostMapping("getList")
    public ResponseDto<?> getList(@RequestBody Map<String,String> param){
        String myId = param.get("myId");
        System.out.println(myId);
        try {
            if(myId==null)  throw new Exception("确少参数myId");

//            QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("is_deleted", false);
//            queryWrapper.eq("my_id", myId);
//            queryWrapper.select("id","friend_id","notation","session_id","last_message","unread","is_hidden","update_time");
//            queryWrapper.orderByDesc("update_time");
            return new ResponseDto<>(friendMapper.getFriendList(myId));
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    ///修改备注
    @PostMapping("/edit")
    public ResponseDto<?> edit(@RequestBody Map<String,String> param){
        String id = param.get("id");
        String notation = param.get("notation");
        try {
            Friend friend = friendMapper.selectById(id);
            friend.setNotation(notation);
            return new ResponseDto<>(friendMapper.updateById(friend));
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ClearParam{
        private String myId;
        private String friendId;
    }
    ////清空已读
    @PostMapping("/clearUnread")
    public ResponseDto<?> clear(@RequestBody Map<String,String> param){
        String myId = param.get("myId");
        String friendId = param.get("friendId");
        try {
            if(myId==null||friendId==null) throw new Exception("缺少参数！");
            friendMapper.clearUnread(new ClearParam(myId, friendId));
            return new ResponseDto<>(null);
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }
}
