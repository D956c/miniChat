package com.ddchat_server.mapper;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ddchat_server.controller.dto.ArticleDto;
import com.ddchat_server.controller.dto.ArticleQuery;
import com.ddchat_server.controller.dto.CommentDto;
import com.ddchat_server.entity.Article;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    //删除说说
    @Delete("UPDATE db_article\n" +
            "SET is_deleted=1\n" +
            "WHERE id=#{id}")
    int deleteArticle(String id);
    @Select("SELECT a.id as id,u.id as userId,u.avatar as avatar,u.nickname as `name`,a.publish_time as publishTime,a.content as content,a.img_list as imgList,a.view_num as viewNum,a.authority as authority \n" +
            "FROM db_article a,db_user u \n" +
            "WHERE a.user_id=u.id AND a.is_deleted=0 AND u.id=#{personId}\n" +
            "${condition}"+
            "ORDER BY id DESC LIMIT 10")
    List<ArticleDto> selectPersonAllArticles(ArticleQuery articleQuery);

    @Select("SELECT user_id\n" +
            "FROM db_comment_like\n" +
            "where comment_id=#{commentId} and is_deleted=0")
    List<Long> selectCommentLikeUserId(String commentId);
    @Select("SELECT c.id as id,c.user_id as user_id,u.nickname as nickname,u.avatar as avatar,c.content as content,c.publish_time as publishTime\n" +
            "FROM db_comment c,db_user u\n" +
            "WHERE c.user_id=u.id \n" +
            "AND c.is_deleted=0 AND c.article_id=#{articleId}\n" +
            "ORDER BY c.id ASC")
    List<CommentDto> selectComments(ArticleQuery articleQuery);

    @Select("SELECT c.id as id,c.user_id as user_id,u.nickname as nickname,u.avatar as avatar,c.content as content,c.publish_time as publishTime " +
            "FROM db_comment c,db_user u " +
            "WHERE c.user_id=u.id AND c.id=#{commentId}")
    CommentDto selectOneComment(String commentId);
    @Update("UPDATE db_article\n" +
            "SET view_num=view_num+1\n" +
            "WHERE id=#{articleId}")
    void addViewNum(String articleId);
    @Select("SELECT a.id as id,u.id as userId,u.avatar as avatar,u.nickname as `name`,a.publish_time as publishTime,a.content as content,a.img_list as imgList,a.view_num as viewNum " +
            "FROM db_article a,db_user u " +
            "WHERE a.user_id=u.id AND a.id=#{articleId}")
    ArticleDto selectOneArticle(String articleId);
    ///全世界 查询最新的记录
    @Select("SELECT a.id as id,u.id as userId,u.avatar as avatar,u.nickname as `name`,a.publish_time as publishTime,a.content as content,a.img_list as imgList,a.view_num as viewNum " +
            "FROM db_article a,db_user u " +
            "WHERE a.user_id=u.id AND a.is_deleted=0 " +
            "AND(a.user_id=#{userId} OR a.authority=0 OR(a.authority=1 AND a.user_id IN( " +
            "SELECT friend_id FROM db_friend WHERE my_id=#{userId} AND is_deleted=0 " +
            "))) " +
            "ORDER BY a.id DESC LIMIT 10")
    List<ArticleDto> selectLast(ArticleQuery articleQuery);

    ///全世界 查询历史记录
    @Select("SELECT a.id as id,u.id as userId,u.avatar as avatar,u.nickname as `name`,a.publish_time as publishTime,a.content as content,a.img_list as imgList,a.view_num as viewNum\n" +
            "FROM db_article a,db_user u\n" +
            "WHERE a.user_id=u.id AND a.is_deleted=0\n" +
            "AND(a.user_id=#{userId} OR a.authority=0 OR(a.authority=1 AND a.user_id IN(\n" +
            "\tSELECT friend_id FROM db_friend WHERE my_id=#{userId} AND is_deleted=0\n" +
            ")))\n" +
            "AND a.id<#{indexId}\n" +
            "ORDER BY a.id DESC LIMIT 10")
    List<ArticleDto> selectHistory(ArticleQuery articleQuery);
    ///仅好友 查询最新记录
    @Select("SELECT a.id as id,u.id as userId,u.avatar as avatar,u.nickname as `name`,a.publish_time as publishTime,a.content as content,a.img_list as imgList,a.view_num as viewNum\n" +
            "FROM db_article a,db_user u\n" +
            "WHERE a.user_id=u.id AND a.is_deleted=0\n" +
            "AND (a.user_id=#{userId} OR a.user_id IN(\n" +
            "\tSELECT friend_id FROM db_friend WHERE my_id=#{userId} AND is_deleted=0\n" +
            "))\n" +
            "ORDER BY a.id DESC LIMIT 10\n")
    List<ArticleDto> selectFriendLast(ArticleQuery articleQuery);
    ///仅好友 查询最新记录
    @Select("SELECT a.id as id,u.id as userId,u.avatar as avatar,u.nickname as `name`,a.publish_time as publishTime,a.content as content,a.img_list as imgList,a.view_num as viewNum\n" +
            "FROM db_article a,db_user u\n" +
            "WHERE a.user_id=u.id AND a.is_deleted=0\n" +
            "AND(a.user_id=#{userId} OR  a.user_id IN(\n" +
            "\tSELECT friend_id FROM db_friend WHERE my_id=#{userId} AND is_deleted=0\n" +
            "))\n" +
            "AND a.id<#{indexId}\n" +
            "ORDER BY a.id DESC LIMIT 10\n")
    List<ArticleDto> selectFriendHistory(ArticleQuery articleQuery);

    @Select("SELECT user_id\n" +
            "FROM db_comment\n" +
            "where article_id=#{articleId} and is_deleted=0")
    List<Long> selectCommentUserId(String articleId);

    @Select("SELECT user_id\n" +
            "FROM db_article_like\n" +
            "where article_id=#{articleId} and is_deleted=0")
    List<Long> selectLikeUserId(String articleId);

    //取消点赞
    @Update("UPDATE db_article_like\n" +
            "set is_deleted=1\n" +
            "where user_id=#{userId} and article_id=#{articleId}")
    int cancelArticleLike(ArticleQuery articleQuery);

    @Update("UPDATE db_comment_like\n" +
            "set is_deleted=1\n" +
            "where comment_id=#{commentId}")
    int cancelCommentLike(ArticleQuery articleQuery);

}
