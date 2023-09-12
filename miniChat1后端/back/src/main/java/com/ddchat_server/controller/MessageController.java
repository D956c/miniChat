package com.ddchat_server.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddchat_server.entity.GroupMessage;
import com.ddchat_server.controller.dto.ResponseDto;
import com.ddchat_server.entity.Message;
import com.ddchat_server.mapper.GroupMessageMapper;
import com.ddchat_server.mapper.MessageMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/ddchat/message")
public class MessageController {
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private GroupMessageMapper groupMessageMapper;

    ///获取用户消息
    @PostMapping("/person")
    public ResponseDto<?> person(@RequestBody Map<String,String> param){
        try{
            String pageSize = param.get("pageSize");
            String pageNum = param.get("pageNum");
            String sessionId = param.get("sessionId");
            String lastMessageId = param.get("lastMessageId");
            if(pageSize==null||pageNum==null||sessionId==null) throw new Exception("获取消息时参数缺失");
            IPage<Message> page = new Page<>( Integer.parseInt(pageNum),Integer.parseInt(pageSize));
            QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("is_deleted", false).eq("session_id",sessionId);
            queryWrapper.orderByDesc("id");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sessionId",sessionId);
            if(lastMessageId!=null){
                queryWrapper.le("id", lastMessageId);
            }
            jsonObject.put("pageInfo",messageMapper.selectPage(page, queryWrapper));
            return new ResponseDto<>(jsonObject);
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    //获取群聊消息
    ///获取用户消息
    @PostMapping("/group")
    public ResponseDto<?> group(@RequestBody Map<String,String> param){
        try{
            String pageSize = param.get("pageSize");
            String pageNum = param.get("pageNum");
            String groupId = param.get("groupId");
            String lastMessageId = param.get("lastMessageId");
            if(pageSize==null||pageNum==null||groupId==null) throw new Exception("获群取消息时参数缺失");
            IPage<GroupMessage> page = new Page<>(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
            QueryWrapper<GroupMessage> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("is_deleted", false).eq("group_id",groupId);
            queryWrapper.orderByDesc("id");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("groupId",groupId);
            if(lastMessageId!=null){
                queryWrapper.le("id", lastMessageId);
            }
            jsonObject.put("pageInfo",groupMessageMapper.selectPage(page, queryWrapper));
            return new ResponseDto<>(jsonObject);
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }
}
