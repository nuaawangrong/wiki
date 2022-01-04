package com.wangrong.wiki.service;

import com.wangrong.wiki.websocket.WebSocketServer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WsService {

    @Resource
    public WebSocketServer webSocketServer;

    @Async
    public void sendInfo(String message) {
        webSocketServer.sendInfo(message);
    }

//    @Async
//    public void sendInfo(String message, String logId) {
//        MDC.put("LOG_ID", logId);
//        webSocketServer.sendInfo(message);
//    }
}
