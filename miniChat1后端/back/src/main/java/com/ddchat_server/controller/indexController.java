package com.ddchat_server.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddchat_server.controller.dto.ResponseDto;

import com.ddchat_server.entity.*;
import com.ddchat_server.mapper.*;
import com.ddchat_server.util.JWTUtils;
import com.ddchat_server.util.Md5Utils;
import com.ddchat_server.util.VerifyUtil;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ddchat/index")
public class indexController {

    @Resource
    private UserMapper userMapper;
    @Resource
    private LogMapper logMapper;
    @Resource
    private AuthorityMapper authorityMapper;

    @Resource
    private FriendMapper friendMapper;
    ///获取用户基本信息
    @PostMapping("/info")
    public ResponseDto<?> info(@RequestBody User user)
    {
        try {
            User user1 = userMapper.selectById(user.getId());
            user1.clearPrivateInfo();
            return new ResponseDto<>(user1);
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }
    //获取好友列表
    @PostMapping("getList")
    public ResponseDto<?> getList(@RequestBody Map<String,String> param){
        String myId = param.get("myId");
        try {
            if(myId==null)  throw new Exception("确少参数myId");
            return new ResponseDto<>(friendMapper.getFriendList(myId));
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    //注册新用户
    @PostMapping("/register")
    public ResponseDto<?> register(@RequestBody Map<String,String> param){
        String email = param.get("email");
        String nickname = param.get("nickname");
        String gender = param.get("gender");
        String password1 = param.get("password1");
        String password2 = param.get("password2");
        System.out.println("提交的信息"+email+nickname);
        int GENDER;
        try{
            ///数据格式验证
            if(VerifyUtil.emailVerify(email)!=null) throw new Exception(VerifyUtil.emailVerify(email));
            if (VerifyUtil.nicknameVerify(nickname)!=null) throw new Exception(VerifyUtil.nicknameVerify(nickname));
            if (VerifyUtil.passwordVerify(password1)!=null) throw new Exception(VerifyUtil.passwordVerify(password1));
            GENDER = VerifyUtil.genderVerify(gender);
            if(GENDER==-1) throw new Exception("性别限制为男女");
            if(!password1.equals(password2)) throw new Exception("前后密码不一致");

            //数据库验证
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", email).eq("is_deleted", false);
            List<User> list = userMapper.selectList(queryWrapper);
            if(list.size()!=0) throw new Exception("该邮箱已注册！");

            User user = new User(null,email,nickname,"",GENDER, Md5Utils.encrypt(password1),"",new Date(),"",true);
            userMapper.insert(user);
        }catch (Exception e){
            return new ResponseDto<>(false,e.getMessage());
        }
        //生成日志
        logMapper.insert(new Log(null,"user-register",null,String.format("邮箱:%s注册账号，昵称为：%s",email,nickname)));
        return new ResponseDto<>("注册成功！");
    }


    //用户登录
    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody Map<String,String> param){
        String email = param.get("email");
        String password = param.get("password");
        ResponseDto<JSONObject> responseDto = new ResponseDto<>(null);
        User user;
        try{
            ///数据格式验证
            if(VerifyUtil.emailVerify(email)!=null) throw new Exception(VerifyUtil.emailVerify(email));
            if (VerifyUtil.passwordVerify(password)!=null) throw new Exception(VerifyUtil.passwordVerify(password));
            //数据库验证
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", email).eq("is_deleted", false).orderByAsc("create_time");
            List<User> list = userMapper.selectList(queryWrapper);
            if(list.size()==0)  throw new Exception("该邮箱未注册！");
            //if(list.size() != 1) throw new Exception("数据库查询出错！");
            user = list.get(0);
            if(!Md5Utils.encrypt(password).equals(user.getPassword())) throw new Exception("密码错误");
            ////检验账号是否激活
            if(!user.getIsActivated()) throw new Exception("该账户未激活！");
            ///////////检验登录权限
            QueryWrapper<Authority> authorityQueryWrapper = new QueryWrapper<>();
            authorityQueryWrapper.eq("user_id",user.getId()).eq("is_deleted", false).eq("type", "login-forbidden");
            List<Authority> authorities = authorityMapper.selectList(authorityQueryWrapper);
            if(authorities.size()!=0) throw new Exception("您的账号已被封禁");
            /////////登录成功生成token
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId().toString());
            String token = JWTUtils.getToken(map);
            user.setPassword(null);
            user.setIsDeleted(null);
            user.setIsActivated(null);
            user.setUpdateTime(null);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token", token);
            jsonObject.put("userInfo", user);
            responseDto.setData(jsonObject);
        }catch (Exception e){
            return new ResponseDto<>(false,e.getMessage());
        }
        //生成日志
        //生成日志
        logMapper.insert(new Log(null,"user-login",user.getId(),String.format("邮箱:%s，昵称为：%s",user.getEmail(),user.getNickname())));
        return responseDto;
    }
}
