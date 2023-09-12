package com.ddchat_server.mapper;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ddchat_server.entity.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GroupMapper extends BaseMapper<Group> {

    @Select("SELECT m.*,g.avatar,g.name " +
            "from db_member m,db_group g " +
            "where m.group_id=g.id and m.is_deleted=0 and member_id=#{id}")
    List<JSONObject> getList(String id);

    //获取用户成员的数据
//    @Select("SELECT u.id,u.avatar,u.nickname,g.owner_id" +
//            "from db_user u,db_member m,db_group g" +
//            "where m.is_deleted=0 and u.id=m.member_id and g.id=m.group_id and m.group_id=#{id} " +
//            "ORDER BY m.create_time ASC ")
    @Select("SELECT u.id,u.avatar,u.nickname,g.owner_id\n" +
            "FROM db_user u,db_member m,db_group g\n" +
            "WHERE m.is_deleted=0 AND u.id=m.member_id AND g.id=m.group_id AND m.group_id=#{id}\n" +
            "ORDER BY m.create_time ASC ")
    List<JSONObject> getMembers(String id);

    @Select("SELECT g.* " +
            "FROM db_group g " +
            "WHERE g.id = #{id}")
    Group getById(String id);
}
