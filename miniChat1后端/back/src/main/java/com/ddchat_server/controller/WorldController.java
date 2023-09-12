package com.ddchat_server.controller;


import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddchat_server.controller.dto.ArticleDto;
import com.ddchat_server.controller.dto.ArticleQuery;
import com.ddchat_server.controller.dto.CommentDto;
import com.ddchat_server.controller.dto.ResponseDto;
import com.ddchat_server.entity.*;
import com.ddchat_server.mapper.*;
import com.ddchat_server.util.JWTUtils;
import com.ddchat_server.util.SnowFlakeUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ddchat/world")
public class WorldController {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArticleLikeMapper articleLikeMapper;
    @Resource
    private CommentLikeMapper commentLikeMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private FriendMapper friendMapper;

    //修改说说权限
    ///删除评论
    @PostMapping("/article/change")
    public ResponseDto<?> changeArticle(@RequestHeader Map<String,String> headers,@RequestBody ArticleQuery articleQuery){
        try {
            String token = headers.get("token");
            if(token==null||!JWTUtils.verify(token)){
                ResponseDto<String> responseDto = new ResponseDto<>(false,"身份验证失败");
                responseDto.setCode("E1");
                return responseDto;
            }
            JSONObject jsonObject = JWTUtils.parseJWT(token);
            String userId = jsonObject.getStr("id");
            if(articleQuery.getArticleId()==null) throw new Exception("参数缺失articleId");
            Article article = articleMapper.selectById(articleQuery.getArticleId());
            if(article==null) throw new Exception("修改失败！未找到记录");
            if(!article.getUserId().equals(userId)) throw new Exception("修改失败！没有权限！");
            if(article.getAuthority()==0)
                article.setAuthority(1);
            else article.setAuthority(0);
            int count = articleMapper.updateById(article);
            if(count>0)
                return new ResponseDto<>(article.getAuthority());
            return new ResponseDto<>(false, "修改失败！");
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    ///删除评论
    @PostMapping("/article/delete")
    public ResponseDto<?> deleteArticle(@RequestHeader Map<String,String> headers,@RequestBody ArticleQuery articleQuery){
        try {
            String token = headers.get("token");
            if(token==null||!JWTUtils.verify(token)){
                ResponseDto<String> responseDto = new ResponseDto<>(false,"身份验证失败");
                responseDto.setCode("E1");
                return responseDto;
            }
            JSONObject jsonObject = JWTUtils.parseJWT(token);
            String userId = jsonObject.getStr("id");
            if(articleQuery.getArticleId()==null) throw new Exception("参数缺失articleId");
            Article article = articleMapper.selectById(articleQuery.getArticleId());
            if(article==null) throw new Exception("删除失败！未找到记录");
            if(!article.getUserId().equals(userId)) throw new Exception("删除失败！没有权限！");
            int count = articleMapper.deleteArticle(articleQuery.getArticleId());
            if(count>0)
                return new ResponseDto<>("删除成功！");
            return new ResponseDto<>(false, "删除失败！");
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    ///发布说说
    @PostMapping("/publish")
    public ResponseDto<?> publish(@RequestHeader Map<String,String> headers, @RequestBody Map<String,String> param){
        try {
            String token = headers.get("token");
            if(token==null||!JWTUtils.verify(token)){
                ResponseDto<String> responseDto = new ResponseDto<>(false,"身份验证失败");
                responseDto.setCode("E1");
                return responseDto;
            }
            JSONObject jsonObject = JWTUtils.parseJWT(token);
            String userId = jsonObject.getStr("id");
            String authority = param.get("authority");
            String content = param.get("content");
            String imgList = param.get("imgList");
            if(content==null||content.trim().equals("")){
                throw new Exception("内容不能为空！");
            }
            if(imgList==null) imgList = "[]";
            if(authority==null) authority = "0";
            ///创建说说
            Article article = new Article(null,userId,Integer.parseInt(authority),String.valueOf(new Date().getTime()),content,imgList,0);
            int num = articleMapper.insert(article);
            if(num==1) return new ResponseDto<>("发布成功！");
            throw new Exception("发布失败！");
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    //获取具体的某个人的说说
    @PostMapping("/person/articles")
    public ResponseDto<?> personArticles(@RequestHeader Map<String,String> headers, @RequestBody ArticleQuery articleQuery){
        try {
            String token = headers.get("token");
            if(token==null||!JWTUtils.verify(token)){
                ResponseDto<String> responseDto = new ResponseDto<>(false,"身份验证失败");
                responseDto.setCode("E1");
                return responseDto;
            }
            JSONObject jsonObject = JWTUtils.parseJWT(token);
            String userId = jsonObject.getStr("id");
            if(articleQuery.getPersonId()==null) throw new Exception("personId参数缺失！");
            if(articleQuery.getIndexId()==null) throw new Exception("indexId参数缺失！");
            if(!articleQuery.getIndexId().equals("0"))
                articleQuery.setCondition(String.format("\nAND a.id<%s\n",articleQuery.getIndexId()));
            if(userId.equals(articleQuery.getPersonId())){
                //请求自己的
                List<ArticleDto> articleDtoList = articleMapper.selectPersonAllArticles(articleQuery);
                for(ArticleDto articleDto : articleDtoList){
                    List<Long> commentIds = articleMapper.selectCommentUserId(articleDto.getId());
                    List<Long> likeIds = articleMapper.selectLikeUserId(articleDto.getId());
                    articleDto.setCommentNum(commentIds.size());
                    articleDto.setLikeNum(likeIds.size());
                    articleDto.setIsLike(likeIds.contains(Long.parseLong(userId)));
                    articleDto.setIsFriend(true);
                }
                return new ResponseDto<>(articleDtoList);
            }else{
                ///先看看是否是自己的朋友
                QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("my_id", userId).eq("friend_id",articleQuery.getPersonId()).eq("is_deleted",false);
                int count = friendMapper.selectCount(queryWrapper);
                boolean isFriend = count>0;
                if(!isFriend){
                    articleQuery.setCondition(articleQuery.getCondition()+"\nAND a.authority=0\n");
                }
                List<ArticleDto> articleDtoList = articleMapper.selectPersonAllArticles(articleQuery);
                for(ArticleDto articleDto : articleDtoList){
                    List<Long> commentIds = articleMapper.selectCommentUserId(articleDto.getId());
                    List<Long> likeIds = articleMapper.selectLikeUserId(articleDto.getId());
                    articleDto.setCommentNum(commentIds.size());
                    articleDto.setLikeNum(likeIds.size());
                    articleDto.setIsLike(likeIds.contains(Long.parseLong(userId)));
                    articleDto.setIsFriend(isFriend);
                }
                return new ResponseDto<>(articleDtoList);
            }
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    //获取某一条具体的说说的内容
    @PostMapping("/article")
    public ResponseDto<?> article(@RequestHeader Map<String,String> headers, @RequestBody ArticleQuery articleQuery){
        try {
            String token = headers.get("token");
            if(token==null||!JWTUtils.verify(token)){
                ResponseDto<String> responseDto = new ResponseDto<>(false,"身份验证失败");
                responseDto.setCode("E1");
                return responseDto;
            }
            JSONObject jsonObject = JWTUtils.parseJWT(token);
            String userId = jsonObject.getStr("id");
            if(articleQuery.getArticleId()==null) throw new Exception("articleId参数缺失~");
            ArticleDto articleDto = articleMapper.selectOneArticle(articleQuery.getArticleId());
            List<Long> commentIds = articleMapper.selectCommentUserId(articleDto.getId());
            List<Long> likeIds = articleMapper.selectLikeUserId(articleDto.getId());
            articleDto.setCommentNum(commentIds.size());
            articleDto.setLikeNum(likeIds.size());
            articleDto.setIsLike(likeIds.contains(Long.parseLong(userId)));
            List<String> friendIds = friendMapper.getFriendIds(userId);
            articleDto.setIsFriend(friendIds.contains(articleDto.getUserId()));
            articleDto.setViewNum(articleDto.getViewNum()+1);
            articleMapper.addViewNum(articleQuery.getArticleId());
            return new ResponseDto<>(articleDto);
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    //获取说说列表
    @PostMapping("/articles")
    public ResponseDto<?> articles(@RequestHeader Map<String,String> headers, @RequestBody ArticleQuery articleQuery){
        try {
            String token = headers.get("token");
            if(token==null||!JWTUtils.verify(token)){
                ResponseDto<String> responseDto = new ResponseDto<>(false,"身份验证失败");
                responseDto.setCode("E1");
                return responseDto;
            }
            JSONObject jsonObject = JWTUtils.parseJWT(token);
            String userId = jsonObject.getStr("id");
            articleQuery.setUserId(userId);
            if(articleQuery.getIndexId()==null) throw new Exception("indexId为空！");
            if(articleQuery.getAuthority()==null) throw new Exception("范围错误！");
            List<ArticleDto> articleDtoList;
            if(articleQuery.getAuthority().equals("0")){
                ///查看全世界的
                if(articleQuery.getIndexId().equals("0")){
                    articleDtoList = articleMapper.selectLast(articleQuery);
                }else{
                    articleDtoList = articleMapper.selectHistory(articleQuery);
                }
            }else{
                if(articleQuery.getIndexId().equals("0")){
                    articleDtoList = articleMapper.selectFriendLast(articleQuery);
                }else{
                    articleDtoList = articleMapper.selectFriendHistory(articleQuery);
                }
            }
            List<String> friendIds = friendMapper.getFriendIds(userId);
            for(ArticleDto articleDto : articleDtoList){
                List<Long> commentIds = articleMapper.selectCommentUserId(articleDto.getId());
                List<Long> likeIds = articleMapper.selectLikeUserId(articleDto.getId());
                articleDto.setCommentNum(commentIds.size());
                articleDto.setLikeNum(likeIds.size());
                articleDto.setIsLike(likeIds.contains(Long.parseLong(userId)));
                articleDto.setIsFriend(friendIds.contains(articleDto.getUserId()));
            }
            ///返回数据
            return new ResponseDto<>(articleDtoList);
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    //给说说点赞/取消
    @PostMapping("/like/article")
    public ResponseDto<?> likeArticle(@RequestHeader Map<String,String> headers, @RequestBody ArticleQuery articleQuery){
        try {
            String token = headers.get("token");
            if(token==null||!JWTUtils.verify(token)){
                ResponseDto<String> responseDto = new ResponseDto<>(false,"身份验证失败");
                responseDto.setCode("E1");
                return responseDto;
            }
            JSONObject jsonObject = JWTUtils.parseJWT(token);
            String userId = jsonObject.getStr("id");
            String articleId = articleQuery.getArticleId();
            if(articleId==null) throw new Exception("缺少articleId！");
            String type = articleQuery.getType();
            if(type==null) throw new Exception("缺少type字段！");
            if(type.equals("like")){
                QueryWrapper<ArticleLike> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id",userId).eq("article_id", articleId).eq("is_deleted", false);
                Integer count = articleLikeMapper.selectCount(queryWrapper);
                if(count!=0) throw new Exception("你已经点过赞啦~");
                int num = articleLikeMapper.insert(new ArticleLike(null,userId,articleId));
                if(num==1) return new ResponseDto<>("点赞成功！");
                throw new Exception("点赞失败！");
            }else {
                articleQuery.setUserId(userId);
                int count = articleMapper.cancelArticleLike(articleQuery);
                if(count>0) return new ResponseDto<>("取消点赞");
                throw new Exception("取消点赞失败！");
            }
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }

    //发布评论
    @PostMapping("/comment/publish")
    public ResponseDto<?> publishComment(@RequestHeader Map<String,String> headers, @RequestBody Map<String,String> param){
        try {
            String token = headers.get("token");
            if(token==null||!JWTUtils.verify(token)){
                ResponseDto<String> responseDto = new ResponseDto<>(false,"身份验证失败");
                responseDto.setCode("E1");
                return responseDto;
            }
            JSONObject jsonObject = JWTUtils.parseJWT(token);
            String userId = jsonObject.getStr("id");
            String articleId = param.get("articleId");
            String content = param.get("content");
            if(content==null||content.trim().equals("")){
                throw new Exception("内容不能为空！");
            }
            if(content.length()>100) throw new Exception("评论不能超过100个字！");
            if(articleId==null) throw new Exception("articleId参数缺失！");
            ///创建评论
            Comment comment = new Comment(SnowFlakeUtil.getSnowFlakeId().toString(),articleId,userId,content,String.valueOf(new Date().getTime()));
            int num = commentMapper.insert(comment);
            CommentDto commentDto = articleMapper.selectOneComment(comment.getId());
            commentDto.setLikeNum(0);
            commentDto.setIsLike(false);
            if(num==1) return new ResponseDto<>(commentDto);
            throw new Exception("发布失败！");
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }
    ///删除评论
    @PostMapping("/comment/delete")
    public ResponseDto<?> deleteComment(@RequestBody ArticleQuery articleQuery){
        try {
            if(articleQuery.getCommentId()==null) throw new Exception("参数缺失commentId");
            QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id",articleQuery.getCommentId());
            Comment comment = commentMapper.selectOne(queryWrapper);
            if(comment==null) throw new Exception("删除失败");
            comment.setIsDeleted(true);
            commentMapper.updateById(comment);
            return new ResponseDto<>("删除成功！");
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }
    //给评论点赞/取消
    @PostMapping("/like/comment")
    public ResponseDto<?> likeComment(@RequestHeader Map<String,String> headers, @RequestBody ArticleQuery articleQuery){
        try {
            String token = headers.get("token");
            if(token==null||!JWTUtils.verify(token)){
                ResponseDto<String> responseDto = new ResponseDto<>(false,"身份验证失败");
                responseDto.setCode("E1");
                return responseDto;
            }
            JSONObject jsonObject = JWTUtils.parseJWT(token);
            String userId = jsonObject.getStr("id");
            String commentId = articleQuery.getCommentId();
            if(commentId==null) throw new Exception("缺少articleId！");
            String type = articleQuery.getType();
            if(type==null) throw new Exception("缺少type字段！");
            if(type.equals("like")){
                QueryWrapper<CommentLike> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id",userId).eq("comment_id", commentId).eq("is_deleted", false);
                Integer count = commentLikeMapper.selectCount(queryWrapper);
                if(count!=0) throw new Exception("你已经点过赞啦~");
                int num = commentLikeMapper.insert(new CommentLike(null,commentId,userId));
                if(num==1) return new ResponseDto<>("点赞成功！");
                throw new Exception("点赞失败！");
            }else {
                articleQuery.setUserId(userId);
                int count = articleMapper.cancelCommentLike(articleQuery);
                if(count>0) return new ResponseDto<>("取消点赞");
                throw new Exception("取消点赞失败！");
            }
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }
    @PostMapping("/comments")
    public ResponseDto<?> comments(@RequestHeader Map<String,String> headers, @RequestBody ArticleQuery articleQuery){
        try {
            String token = headers.get("token");
            if(token==null||!JWTUtils.verify(token)){
                ResponseDto<String> responseDto = new ResponseDto<>(false,"身份验证失败");
                responseDto.setCode("E1");
                return responseDto;
            }
            JSONObject jsonObject = JWTUtils.parseJWT(token);
            String userId = jsonObject.getStr("id");
            articleQuery.setUserId(userId);
            if(articleQuery.getArticleId()==null) throw new Exception("获取评论失败");
            List<CommentDto> commentDtoList = articleMapper.selectComments(articleQuery);
            for(CommentDto commentDto : commentDtoList){
                List<Long> likeIds = articleMapper.selectCommentLikeUserId(commentDto.getId());
                commentDto.setIsLike(likeIds.contains(Long.parseLong(userId)));
                commentDto.setLikeNum(likeIds.size());
            }
            return new ResponseDto<>(commentDtoList);
        }catch (Exception e){
            return new ResponseDto<>(false, e.getMessage());
        }
    }
}
