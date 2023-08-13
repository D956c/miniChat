package com.ddchat_server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ddchat_server.entity.Notice;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface NoticeMapper extends BaseMapper<Notice> {
    @Update("update db_notice " +
            "set is_deleted=1 " +
            "where id=#{id}")
    void deleteNotice(String id);
    @Update("update db_notice " +
            "set is_read=1 " +
            "where id=#{id}")
    void checkNotice(String id);
}
