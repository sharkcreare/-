package com.pzx.knowledge.websocket;

import com.pzx.knowledge.utils.JwtUtils;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@Component
@ServerEndpoint("/ws/notification")
public class NotificationHandler {

    private static final Map <Long, Session> ONLINE_USERS =new ConcurrentHashMap<>();




    /** 解析握手 URL 的 token，并校验 */
    @OnOpen
    public void onOpen(Session session) {

        Long userId = authenticate(session);
        if (userId == null) {
            log.warn("WebSocket 连接鉴权失败，关闭连接");
            try {
                session.close();
            }catch (Exception ignored){}
            return;
        }
        Session old =ONLINE_USERS.put(userId,session);
        if (old != null) {
            try { old.close(); } catch (Exception ignored) { }
        }
        log.info("WebSocket建立连接：userId={}",userId);
    }



    @OnClose
    public void onClose(Session session) {
        ONLINE_USERS.values().remove(session);
        log.info("WebSocket 连接关闭");
    }
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket 错误",error);
        ONLINE_USERS.values().remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
     log.info("收到消息：{}",message);
    }


    private Long authenticate(Session session) {
        String query =session.getQueryString();
        if (query == null) {
            return null;
        }
        String token =null;
        for(String pair:query.split("&")){
            String[]kv =pair.split("=",2);
            if(kv.length==2 &&"token".equals(kv[0])){
                token=kv[1];
                break;
            }
        }
        return JwtUtils.getUserIdFromTokenQuietly(token);
    }
    public static void  sendToUsers(Long userId,String message){
        Session session = ONLINE_USERS.get(userId);
        if (session != null&&session.isOpen()) {
            session.getAsyncRemote().sendText(message);
        }
    }
}
