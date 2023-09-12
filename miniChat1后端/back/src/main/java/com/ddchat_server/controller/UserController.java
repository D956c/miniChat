package com.ddchat_server.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddchat_server.entity.Group;
import com.ddchat_server.entity.User;
import com.ddchat_server.controller.dto.ResponseDto;
import com.ddchat_server.mapper.GroupMapper;
import com.ddchat_server.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ddchat/user")
public class UserController {

    @Resource
    private UserMapper userMapper;
    @Resource
    private GroupMapper groupMapper;

    ///获取用户基本信息
    @PostMapping("/info")
//    public ResponseDto<?> info(@RequestBody User user)
//    {
//        try {
//            User user1 = userMapper.selectById(user.getId());
//            user1.clearPrivateInfo();
//            return new ResponseDto<>(user1);
//        }catch (Exception e){
//            return new ResponseDto<>(false, e.getMessage());
//        }
//    }

//    根据好友id获取好友信息
    public ResponseDto<?> info(@RequestBody Map<String,String> param)
    {
        String friend_id = param.get("friend_id");
        System.out.println(friend_id);
        try {
            User user1 = userMapper.selectById(friend_id);
            user1.clearPrivateInfo();
            return new ResponseDto<>(user1);
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }



    //更新用户信息
    @PostMapping("/update")
    public ResponseDto<?> login(@RequestBody User user){
        user.setCreateTime(null);
        userMapper.updateById(user);
        User user1 = userMapper.selectById(user.getId());
        user1.clearPrivateInfo();
        return new ResponseDto<>(user1);
    }


    ///根据关键字查找用户和群聊


    @PostMapping("/search")
    public ResponseDto<?> search(@RequestBody Map<String,String> param){
        String keyword = param.get("keyword");
        ResponseDto<JSONObject> responseDto = new ResponseDto<>(null);
        try {
            if(keyword==null)   throw new Exception("关键词为空！");

            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("is_deleted", false);
            userQueryWrapper.like("nickname",keyword).or().like("email",keyword).or().eq("id",keyword);
            userQueryWrapper.select("id","nickname","email","avatar");
            List<User> userList = userMapper.selectList(userQueryWrapper);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userList", userList);

            QueryWrapper<Group> groupQueryWrapper = new QueryWrapper<>();
            groupQueryWrapper.eq("is_deleted", false);
            groupQueryWrapper.eq("id",keyword).or().like("name",keyword);
            List<Group> groupList = groupMapper.selectList(groupQueryWrapper);
            jsonObject.put("groupList",groupList);
            responseDto.setData(jsonObject);
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
        return responseDto;
    }

}
