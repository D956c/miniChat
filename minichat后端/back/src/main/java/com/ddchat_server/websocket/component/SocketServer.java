package com.ddchat_server.websocket.component;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ddchat_server.entity.*;
import com.ddchat_server.entity.TO.MessageTo;
import com.ddchat_server.mapper.*;
import com.ddchat_server.service.FriendService;
import com.ddchat_server.service.MessageService;
import com.ddchat_server.util.JWTUtils;
import com.ddchat_server.util.SnowFlakeUtil;
import com.ddchat_server.websocket.domain.SocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/socket/{token}")
@Component
public class SocketServer {

    ///用来保存,用户的session
    public static Map<String, Session> userSessionMap = new ConcurrentHashMap<>();
    public static MessageMapper messageMapper;
    public static NoticeMapper noticeMapper;
    public static FriendMapper friendMapper;
    public static FriendService friendService;
    public static MessageService messageService;
    public static MemberMapper memberMapper;
    public static GroupMapper groupMapper ;

    @Autowired
    public void setMemberMapper(MemberMapper memberMapper) {
        SocketServer.memberMapper = memberMapper;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        SocketServer.messageService = messageService;
    }

    @Autowired
    public void setFriendService(FriendService friendService) {
        SocketServer.friendService = friendService;
    }

    @Autowired
    public void setFriendMapper(FriendMapper friendMapper) {
        SocketServer.friendMapper = friendMapper;
    }

    @Autowired
    public void setMessageMapper(MessageMapper messageMapper) {
        SocketServer.messageMapper = messageMapper;
    }

    @Autowired
    void setNoticeMapper(NoticeMapper noticeMapper) {
        SocketServer.noticeMapper = noticeMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        ///检查token是否生效
        System.out.println(Thread.currentThread().getName());
        System.out.println(this.hashCode());
        String id;
        try {
            if (!JWTUtils.verify(token)) throw new Exception("身份验证失败");
            JSONObject jsonObject = JWTUtils.parseJWT(token);
            id = jsonObject.getStr("id");
        } catch (Exception e) {
            this.sendMessage(new SocketMessage<>("token-failed", null), session);
            //session.close();
            return;
        }
        userSessionMap.put(id, session);
        System.out.println("用户：" + id + "已经上线");
        System.out.println("session是什么" + session);
        this.sendToAll(new SocketMessage<>("count", userSessionMap.keySet().toArray()));
    }

    @OnClose
    public void onClose(Session session, @PathParam("token") String token) {
        String id = JWTUtils.parseJWT(token).getStr("id");
        userSessionMap.remove(id);
        System.out.println("用户：" + id + "已经离线");
        this.sendToAll(new SocketMessage<>("count", userSessionMap.keySet().toArray()));

    }

    @OnError
    public void onError(Session session, Throwable error, @PathParam("token") String token) {
        String id = JWTUtils.parseJWT(token).getStr("id");
        userSessionMap.remove(id);
        System.out.println("用户：" + id + "已经离线");
        this.sendToAll(new SocketMessage<>("count", userSessionMap.keySet().toArray()));

    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("token") String token) {
        JSONObject m = JSONUtil.parseObj(message);
        //System.out.println(m.get("type"));
        System.out.println("----------------消息m" + m);
        JSONObject data = m.getJSONObject("data");
        System.out.println("----------------消息" + data);
        switch (m.getStr("type")) {
            case "person-apply":
                ////构造通知消息对象
                Notice notice = new Notice(null, data.getLong("senderId"),
                        data.getLong("receiverId"),
                        "person-apply",
                        m.getStr("data"), false);
                noticeMapper.insert(notice);
                this.sendToUserById(data.getStr("receiverId"), new SocketMessage<>("person-apply", m.getStr("data")));
                break;
            case "person-apply-agree":
                Notice notice1 = noticeMapper.selectById(data.getStr("id"));
                JSONObject jsonObject = JSONUtil.parseObj(notice1.getInformation());
                ////检查两人是否为好友
                if (friendService.checkIfFriend(notice1.getSenderId().toString(), notice1.getReceiverId().toString())) {
                    notice1.setIsRead(true);
                    noticeMapper.updateById(notice1);
                    this.sendToUserById(notice1.getReceiverId().toString(), new SocketMessage<>("notice-refresh", "TA已经是你的好友了！"));
                    break;
                }
                ////构造好友关系
                Long sessionId = SnowFlakeUtil.getSnowFlakeId();
                Friend friend1 = new Friend(null, notice1.getSenderId(), notice1.getReceiverId(),
                        jsonObject.getStr("notation"), sessionId, "", 0, false);
                Friend friend2 = new Friend(null, notice1.getReceiverId(), notice1.getSenderId(),
                        jsonObject.getStr("senderName"), sessionId, "", 0, false);
                friendMapper.insert(friend1);
                friendMapper.insert(friend2);
                notice1.setIsRead(true);
                noticeMapper.updateById(notice1);
                ///构造消息
                SocketMessage<Friend> socketMessage = new SocketMessage<>("person-apply-agree", friend2);
                this.sendToUserById(notice1.getSenderId().toString(), socketMessage);
                this.sendToUserById(notice1.getReceiverId().toString(), socketMessage);
                break;
            case "person-message":
                ///////构造消息
                Message message1 = new Message(SnowFlakeUtil.getSnowFlakeId().toString(), data.getStr("sessionId"),
                        data.getStr("senderId"),
                        data.getStr("receiverId"), data.getStr("sendTime"), data.getStr("content"), data.getStr("messageType"),
                        0);
                messageService.storeMessage(message1);
                MessageTo messageTo = new MessageTo(message1.getId(),data.getStr("chat_type"), data.getStr("sessionId"),
                        data.getStr("senderId"),data.getStr("senderName"),data.getStr("senderAvatar"),
                        data.getStr("receiverId"),data.getStr("receiverName"), data.getStr("sendTime"), data.getStr("content"),  data.getStr("messageType"),0);
                //System.out.println("--------个人消息",message1.);
                //this.sendToUserById(message1.getSenderId(),new SocketMessage<>("person-message",message1));
                this.sendToUserById(message1.getReceiverId(), new SocketMessage<>("person-message", messageTo));
                break;
            case "join-group":
                //////先检查该用户是否在群里
                String userId = data.getStr("userId");
                String groupId = data.getStr("groupId");

                QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("member_id", userId).eq("group_id", groupId).eq("is_deleted", false);
                List<Member> members = memberMapper.selectList(queryWrapper);

                if (members.size() != 0) {
                    Member member = members.get(0);
                    this.sendToUserById(member.getMemberId().toString(), new SocketMessage<>("join-success", member));
                    return;
                }
                ////创建关系
                Member member = new Member(null, Long.parseLong(userId), groupId, 0, "", false);
                memberMapper.insert(member);
                this.sendToUserById(member.getMemberId().toString(), new SocketMessage<>("join-success", member));
                break;
            case "group-message":
                ///查找该群的全部用户
                String group_id = data.getStr("groupId");
                QueryWrapper<Member> memberQueryWrapper = new QueryWrapper<>();
                memberQueryWrapper.eq("is_deleted", false).eq("group_id", group_id);
                memberQueryWrapper.select("member_id");
                ////保存聊天记录
                GroupMessage groupMessage = new GroupMessage(SnowFlakeUtil.getSnowFlakeId().toString(), group_id, data.getStr("senderId"), data.getStr("senderName"),
                        data.getStr("avatar"), data.getStr("sendTime"), data.getStr("content"), data.getStr("messageType"), 0);
                messageService.StoreGroupMessage(groupMessage);
                List<Object> ids = memberMapper.selectObjs(memberQueryWrapper);
                for (Object id : ids) {
                    ////发送给每一个人
                    this.sendToUserById(id.toString(), new SocketMessage<>("group-message", groupMessage));
                }
                break;
            ///收到了撤回消息
            case "person-withdraw":
                String messageId = data.getStr("messageId");
                String senderId = data.getStr("senderId");
                String receiverId = data.getStr("receiverId");
                String session_id = data.getStr("sessionId");
                ///更新数据库
                messageService.withdrawMessage(messageId);
                messageService.updateMsgWithdrawnSession(session_id);
                /////发送给用户
                this.sendToUserById(senderId, new SocketMessage<>("person-withdraw", data));
                this.sendToUserById(receiverId, new SocketMessage<>("person-withdraw", data));
            case "group-withdraw":
                String groupMessageId = data.getStr("messageId");
                String groupId1 = data.getStr("groupId");
                ///更新数据库
                messageService.withdrawGroupMessage(groupMessageId);
                messageService.updateGroupMsgWithdrawnSession(groupId1, data.getStr("senderName"));
                /////发送给用户
                QueryWrapper<Member> memberQueryWrapper1 = new QueryWrapper<>();
                memberQueryWrapper1.eq("is_deleted", false).eq("group_id", groupId1);
                memberQueryWrapper1.select("member_id");
                List<Object> ids1 = memberMapper.selectObjs(memberQueryWrapper1);
                for (Object id : ids1) {
                    ////发送给每一个人
                    this.sendToUserById(id.toString(), new SocketMessage<>("group-withdraw", data));
                }
                break;
        }

    }

    public void sendToAll(SocketMessage<?> message) {
        for (Session session : userSessionMap.values()) {
            this.sendMessage(message, session);
        }
    }

    public void sendToUserById(String id, SocketMessage<?> message) {
        for (String key : userSessionMap.keySet()) {
            if (Objects.equals(key, id)) {
                this.sendMessage(message, userSessionMap.get(key));
                return;
            }
        }
    }

    public void sendMessage(SocketMessage<?> message, Session toSession) {
        try {
            if (toSession.isOpen()) {
                synchronized (toSession) {
                    toSession.getAsyncRemote().sendText(JSONUtil.toJsonStr(message));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
