package com.ddchat_server.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ddchat_server.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
}
