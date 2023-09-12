package com.ddchat_server.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ddchat_server.controller.dto.ResponseDto;
import com.ddchat_server.entity.Authority;
import com.ddchat_server.entity.Notice;
import com.ddchat_server.mapper.AuthorityMapper;
import com.ddchat_server.mapper.NoticeMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ddchat/notice")
public class NoticeController {
    @Resource
    private NoticeMapper noticeMapper;
    @Resource
    private AuthorityMapper authorityMapper;
    //获取通知消息 进行分页 ，未读数量
    @PostMapping("/list")
    public ResponseDto<?> list(@RequestBody Map<String,String> param){
        String id = param.get("id");
        try {
            if(id==null) throw new Exception("缺少id字段！");

            ///////////检验登录权限
            QueryWrapper<Authority> authorityQueryWrapper = new QueryWrapper<>();
            authorityQueryWrapper.eq("user_id",id).eq("is_deleted", false).eq("type", "login-forbidden");
            List<Authority> authorities = authorityMapper.selectList(authorityQueryWrapper);
            if(authorities.size()!=0){
                ResponseDto<String> responseDto = new ResponseDto<>(false,"fail");
                responseDto.setCode("E1");
                return responseDto;
            }
            JSONObject jsonObject = new JSONObject();
            ///先查询未读数量
            QueryWrapper<Notice> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("is_deleted", false).eq("receiver_id",id);
            ///查询具体的数据
            queryWrapper.orderByDesc("create_time");
            jsonObject.put("noticeList", noticeMapper.selectList(queryWrapper));
            queryWrapper.eq("is_read",false);
            jsonObject.put("count", noticeMapper.selectCount(queryWrapper));
            return new ResponseDto<>(jsonObject);
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @PostMapping("/delete")
    public ResponseDto<?> delete(@RequestBody Map<String,String> param){
        String id = param.get("id");
        try {
            if(id==null) throw new Exception("缺少id字段！");
            noticeMapper.deleteNotice(id);
            return new ResponseDto<>("删除成功！");
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    @PostMapping("/reject")
    public ResponseDto<?> reject(@RequestBody Map<String,String> param){
        String id = param.get("id");
        String senderName = param.get("senderName");
        try {
            if(id==null) throw new Exception("缺少id字段！");
            Notice notice = noticeMapper.selectById(id);
            notice.setIsRead(true);
            noticeMapper.updateById(notice);
            ///构造拒绝消息
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fatherNoticeId",notice.getId());
            jsonObject.put("senderName",senderName);
            Notice rejectNotice = new Notice(null,notice.getReceiverId(),notice.getSenderId(),"reject", jsonObject.toString(),false);
            noticeMapper.insert(rejectNotice);
            return new ResponseDto<>("删除成功！");
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }
    @PostMapping("/check")
    public ResponseDto<?> check(@RequestBody Map<String,String> param){
        String id = param.get("id");
        try {
            if(id==null) throw new Exception("缺少id字段！");
            noticeMapper.checkNotice(id);
            return new ResponseDto<>("已读成功！");
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }
}
