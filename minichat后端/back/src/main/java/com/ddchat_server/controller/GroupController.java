package com.ddchat_server.controller;

import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddchat_server.controller.dto.ResponseDto;
import com.ddchat_server.entity.Group;
import com.ddchat_server.entity.Member;
import com.ddchat_server.mapper.GroupMapper;
import com.ddchat_server.mapper.MemberMapper;
import com.ddchat_server.util.SnowFlakeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/ddchat/group")
public class GroupController {
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private GroupMapper groupMapper;

    ///获取成员信息
    @PostMapping("/member")
    public ResponseDto<?> member(@RequestBody Map<String,String> param){
        String id = param.get("id");
        try {
            if(id==null) throw new Exception("缺少群聊id");
            return new ResponseDto<>(groupMapper.getMembers(id));
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }
    //获取群聊的信息
    @PostMapping("/info")
    public ResponseDto<?> info(@RequestBody Map<String,String> param){
        String id = param.get("id");
        try {
            if(id==null) throw new Exception("缺少群聊id");
            return new ResponseDto<>(groupMapper.selectById(id));
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @PostMapping("/getList")
    public ResponseDto<?> getList(@RequestBody Map<String,String> param){
        String id = param.get("id");
        try {
            if(id==null) throw new Exception("id字段缺失");
            return new ResponseDto<>(groupMapper.getList(id));
        }catch (Exception e){
            return new ResponseDto<>(false,e.getMessage());
        }
    }

    //更新
    @PostMapping("/update")
    public ResponseDto<?> update(@RequestBody Map<String,String> param){
        String id = param.get("id");
        String name = param.get("name");
        String avatar = param.get("avatar");
        String introduction = param.get("introduction");
        try {
            if(name==null||id==null||introduction==null) throw new Exception("信息不完整");
            Group group = groupMapper.selectById(id);
            group.setName(name);
            group.setIntroduction(introduction);
            if(avatar!=null) group.setAvatar(avatar);
            groupMapper.updateById(group);
            return new ResponseDto<>(null);
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseDto<?> create(@RequestBody Map<String,String> param){
        String ownerId = param.get("id");
        String name = param.get("name");
        String avatar = param.get("avatar");
        //String avatar="http://localhost:9999/resource/image/f6b26402-ae92-43ce-9891-9563fc8a51a6.png";
        String introduction = param.get("introduction");
        try {
            if(name==null||avatar==null||introduction==null) throw new Exception("信息不完整");
            Long id = SnowFlakeUtil.getSnowFlakeId();
            Group group = new Group(id,name,avatar,introduction,Long.parseLong(ownerId));
            groupMapper.insert(group);
            Member member = new Member(null,Long.parseLong(ownerId),id.toString(),0,"你已成功创建群聊",false);
            memberMapper.insert(member);
            return new ResponseDto<>(member);
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberParam{
        private String memberId;
        private String groupId;
    }
    ////清空已读
    @PostMapping("/clearUnread")
    public ResponseDto<?> clear(@RequestBody Map<String,String> param){
        String userId = param.get("userId");
        String groupId = param.get("groupId");
        try {
            if(userId==null||groupId==null) throw new Exception("缺少参数！");
            memberMapper.clearUnread(new MemberParam(userId,groupId));
            return new ResponseDto<>(null);
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }
}
